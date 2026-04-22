const API_BASE = '/api';

// --- Authentication & Initialization ---
const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        try {
            const res = await fetch(`${API_BASE}/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });
            const data = await res.json();
            if (res.ok) {
                localStorage.setItem('user', JSON.stringify(data));
                window.location.href = 'dashboard.html';
            } else { alert(data.message || 'Login Failed'); }
        } catch (err) { alert('Connection Error!'); }
    });
}

// Handle Signup
const signupForm = document.getElementById('signupForm');
if (signupForm) {
    signupForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const email = document.getElementById('sEmail').value;
        const password = document.getElementById('sPassword').value;
        const role = document.getElementById('sRole').value;
        const fullName = document.getElementById('sFullName').value;
        const location = document.getElementById('sLocation').value;
        const contactNumber = document.getElementById('sContact').value;
        const specialization = document.getElementById('sSpecial').value;
        const companyName = document.getElementById('sCompany').value;
        
        const signupBtn = document.getElementById('signupBtn');
        const loader = document.getElementById('loader');
        const errorBox = document.getElementById('errorBox');
        
        // Advanced Validation (Phase 10)
        const validationError = validateSignup(email, password);
        if (validationError) {
            errorBox.textContent = validationError;
            errorBox.style.display = 'block';
            return;
        }

        signupBtn.style.display = 'none';
        loader.style.display = 'block';
        errorBox.style.display = 'none';

        try {
            const res = await fetch(`${API_BASE}/auth/signup`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password, role, fullName, location, contactNumber, specialization, companyName })
            });
            const data = await res.json();
            if (res.ok) {
                alert('Account created! A verification code has been sent to your email.');
                window.location.href = `verify.html?email=${encodeURIComponent(email)}`;
            } else { throw new Error(data.message); }
        } catch (err) {
            errorBox.textContent = err.message;
            errorBox.style.display = 'block';
            signupBtn.style.display = 'block';
            loader.style.display = 'none';
        }
    });
}

// Handle Verification
const verifyForm = document.getElementById('verifyForm');
if (verifyForm) {
    const params = new URLSearchParams(window.location.search);
    const email = params.get('email');
    if (email) document.getElementById('displayEmail').textContent = email;

    verifyForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const code = document.getElementById('vCode').value;
        const errorBox = document.getElementById('errorBox');
        try {
            const res = await fetch(`${API_BASE}/auth/verify?email=${encodeURIComponent(email)}&code=${code}`, { method: 'POST' });
            const data = await res.json();
            if (res.ok) {
                alert('Success! Your account is activated.');
                window.location.href = 'index.html';
            } else { throw new Error(data.message); }
        } catch (err) {
            errorBox.textContent = err.message;
            errorBox.style.display = 'block';
        }
    });
}

async function resendCode() {
    const params = new URLSearchParams(window.location.search);
    const email = params.get('email');
    try {
        const res = await fetch(`${API_BASE}/auth/resend-code?email=${encodeURIComponent(email)}`, { method: 'POST' });
        if (res.ok) alert('New code sent!'); else alert('Failed to resend');
    } catch (err) { alert('Error!'); }
}

document.addEventListener('DOMContentLoaded', () => {
    if (window.location.pathname.endsWith('dashboard.html')) {
        const user = JSON.parse(localStorage.getItem('user'));
        if (!user) { window.location.href = 'index.html'; return; }

        document.getElementById('welcomeTitle').textContent = `Welcome, ${user.name || user.email.split('@')[0]}!`;
        document.getElementById('userSubtext').textContent = user.role;

        if (user.role === 'ADMIN') {
            document.getElementById('navAdmin').style.display = 'block';
            document.getElementById('adminStats').style.display = 'block';
            document.getElementById('standardStats').style.display = 'none';
            fetchUsers(); fetchHistory(); fetchGlobalStats();
        } else if (user.role === 'AGRONOMIST') {
            document.getElementById('navAgronomist').style.display = 'block';
            fetchPendingQuestions();
        } else if (user.role === 'FARMER') {
            document.getElementById('navFarmer').style.display = 'block';
            fetchMyQuestions();
            fetchAgroDealers();
            fetchCropGuide();
        } else if (user.role === 'AGRO_DEALER') {
            document.getElementById('navAgroDealer').style.display = 'block';
            fetchDealerProfile();
        }
    }
});

function showSection(id) {
    document.querySelectorAll('.section').forEach(s => s.style.display = 'none');
    document.getElementById(id).style.display = 'block';
    
    document.querySelectorAll('.nav-link').forEach(l => {
        l.classList.remove('active');
        if (l.innerText.toLowerCase().includes(id.split('-')[0]) || (id === 'overview' && l.innerText.toLowerCase() === 'dashboard')) {
            l.classList.add('active');
        }
    });

    if (id === 'manage-data') fetchHistory();
    if (id === 'expert-answering') fetchPendingQuestions();
    if (id === 'dealers') fetchAgroDealers();
    if (id === 'crop-guide') fetchCropGuide();
    if (id === 'settings') fetchSystemStatus();
    if (id === 'dealer-dashboard') fetchDealerProfile();
}

// --- Farmer Actions ---
async function askQuestion() {
    const user = JSON.parse(localStorage.getItem('user'));
    const payload = {
        farmerId: user.id,
        title: document.getElementById('qTitle').value,
        content: document.getElementById('qContent').value
    };
    const res = await fetch(`${API_BASE}/questions`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${user.token}` },
        body: JSON.stringify(payload)
    });
    if (res.ok) { alert('Consultation Shared!'); document.getElementById('qTitle').value=''; document.getElementById('qContent').value=''; fetchMyQuestions(); }
}

async function fetchMyQuestions() {
    const user = JSON.parse(localStorage.getItem('user'));
    const res = await fetch(`${API_BASE}/questions/farmer/${user.id}`, { headers: { 'Authorization': `Bearer ${user.token}` } });
    const data = await res.json();
    document.getElementById('myQuestions').innerHTML = data.map(q => `
        <div class="forum-card">
            <span class="badge ${q.isResolved ? 'badge-resolved' : 'badge-pending'}">${q.isResolved ? 'RESOLVED' : 'PENDING'}</span>
            <h4 style="margin: 10px 0">${q.title}</h4>
            <p>${q.content}</p>
            ${q.answers ? q.answers.map(a => `<div style="margin-top:10px; padding:10px; background:#fff; border-radius:8px"><strong>Expert Advice:</strong> ${a.content}</div>`).join('') : ''}
        </div>
    `).reverse().join('');
}

async function getRecommendations() {
    const soil = document.getElementById('soilType').value;
    const season = document.getElementById('season').value;
    const res = await fetch(`${API_BASE}/crops/recommend?soil=${soil}&season=${season}`);
    const data = await res.json();
    const container = document.getElementById('recResults');
    container.innerHTML = data.length === 0 ? '<p style="color:red; margin-top:20px">No matching crops.</p>' : data.map(c => `
        <div class="result-item"><h4>${c.name}</h4><p>${c.description}</p></div>
    `).join('');
}

async function fetchDealerProfile() {
    const user = JSON.parse(localStorage.getItem('user'));
    try {
        const res = await fetch(`${API_BASE}/auth/agro-dealers`);
        const data = await res.json();
        // Filter for current user
        const profile = data.find(d => d.id === user.id);
        if (profile) {
            document.getElementById('dealerSpecificTbody').innerHTML = `
                <tr>
                    <td>${profile.id}</td>
                    <td style="font-weight:700; color:var(--primary);">${profile.companyName}</td>
                    <td>📞 ${profile.contactNumber || 'N/A'}</td>
                    <td>📍 ${profile.location || 'N/A'}</td>
                    <td style="color:var(--secondary); font-weight:600;">${profile.fullTexts || 'Expert guidance not yet configured.'}</td>
                </tr>
            `;
        }
    } catch (err) { console.error(err); }
}

async function fetchAgroDealers() {
    try {
        const res = await fetch(`${API_BASE}/auth/agro-dealers`);
        const data = await res.json();
        document.getElementById('dealerTbody').innerHTML = data.length === 0 ? '<tr><td colspan="4" style="text-align:center;">No agro dealers found.</td></tr>' : data.map(d => `
            <tr>
                <td style="font-weight: bold; color: var(--primary);">${d.companyName}</td>
                <td>📍 ${d.location || 'Location Not Specified'}</td>
                <td>📞 ${d.contactNumber || 'No Contact Provided'}</td>
                <td>✉️ <a href="mailto:${d.email}" style="color: var(--secondary); text-decoration: none;">${d.email}</a></td>
            </tr>
        `).join('');
    } catch (err) {
        console.error(err);
    }
}

// --- Agronomist Actions ---
async function fetchPendingQuestions() {
    const user = JSON.parse(localStorage.getItem('user'));
    const res = await fetch(`${API_BASE}/questions`, { headers: { 'Authorization': `Bearer ${user.token}` } });
    const data = await res.json();
    const pending = data.filter(q => !q.isResolved);
    document.getElementById('pendingQuestions').innerHTML = pending.length === 0 ? '<p>No pending consultations.</p>' : pending.map(q => `
        <div class="glass-card">
            <h4>${q.title}</h4><p>${q.content}</p>
            <textarea id="ans-${q.id}" placeholder="Provide expert response..." style="margin:10px 0"></textarea>
            <button class="btn-action" onclick="submitAnswer(${q.id})">SUBMIT ADVICE</button>
        </div>
    `).join('');
}

async function submitAnswer(qId) {
    const user = JSON.parse(localStorage.getItem('user'));
    const content = document.getElementById(`ans-${qId}`).value;
    await fetch(`${API_BASE}/questions/${qId}/answers`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${user.token}` },
        body: JSON.stringify({ agronomistId: user.id, content })
    });
    alert('Advice Sent!'); fetchPendingQuestions();
}

// --- Admin Actions ---
async function handleRegister() {
    const email = document.getElementById('regEmail').value;
    const password = document.getElementById('regPassword').value;
    const role = document.getElementById('regRole').value;
    
    if (!email || !password) {
        alert('Please fill out all fields.');
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/auth/signup`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ 
                email, 
                password, 
                role,
                fullName: 'Provisioned by Admin'
            })
        });
        const data = await res.json();
        
        if (res.ok) {
            alert('User provisioned successfully! A verification link has been sent to their email.');
            document.getElementById('regEmail').value = '';
            document.getElementById('regPassword').value = '';
            fetchUsers();
        } else {
            alert(data.message || 'Failed to provision account.');
        }
    } catch (err) {
        console.error(err);
        alert('Network Error.');
    }
}

async function fetchUsers() {
    const user = JSON.parse(localStorage.getItem('user'));
    const res = await fetch(`${API_BASE}/auth/users`, { headers: { 'Authorization': `Bearer ${user.token}` } });
    const data = await res.json();
    document.getElementById('userTbody').innerHTML = data.map(u => `
        <tr>
            <td style="padding: 15px;">
                <div style="font-weight: 700;">${u.name || 'Individual'}</div>
                <div style="font-size: 0.75rem; color: #64748b;">Joined ${new Date(u.createdAt).toLocaleDateString()}</div>
            </td>
            <td style="padding: 15px;">${u.email}</td>
            <td style="padding: 15px;"><span class="badge" style="background:#f1f5f9; color:#475569;">${u.role}</span></td>
            <td style="padding: 15px;"><span class="badge ${u.isVerified ? 'badge-resolved' : 'badge-pending'}">${u.isVerified ? 'VERIFIED' : 'PENDING'}</span></td>
            <td style="padding: 15px;">
                <span style="font-size: 0.85rem; color: #4b5563;">
                    ${u.lastLoginAt ? new Date(u.lastLoginAt).toLocaleString() : '<span style="color:#94a3b8">Never</span>'}
                </span>
            </td>
            <td style="padding: 15px; text-align: right; display:flex; gap:8px; justify-content: flex-end;">
                <button class="btn-action" style="padding: 6px 12px; font-size: 0.75rem; background: var(--secondary);" onclick='prepareUpdateModal(${JSON.stringify(u)})'>Edit</button>
                <button class="btn-logout" style="padding: 6px 12px; font-size: 0.75rem; margin:0;" onclick="deleteUser(${u.id})">Remove</button>
            </td>
        </tr>`).join('');
}

function prepareUpdateModal(u) {
    document.getElementById('updateId').value = u.id;
    document.getElementById('updateEmail').textContent = u.email;
    document.getElementById('updateVerify').value = u.isVerified.toString();
    document.getElementById('updateRole').value = u.role;
    document.getElementById('updateModal').style.display = 'flex';
}

async function commitUserUpdate() {
    const id = document.getElementById('updateId').value;
    const user = JSON.parse(localStorage.getItem('user'));
    const payload = {
        isVerified: document.getElementById('updateVerify').value,
        role: document.getElementById('updateRole').value
    };

    const res = await fetch(`${API_BASE}/auth/users/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${user.token}` },
        body: JSON.stringify(payload)
    });

    if (res.ok) {
        alert('User profile synchronized successfully.');
        document.getElementById('updateModal').style.display = 'none';
        fetchUsers();
    } else {
        const err = await res.json();
        alert(err.message || 'Synchronization failed.');
    }
}

async function runBackup() {
    const user = JSON.parse(localStorage.getItem('user'));
    window.location.href = `${API_BASE}/maintenance/backup`;
}

async function runCleanup() {
    const user = JSON.parse(localStorage.getItem('user'));
    const res = await fetch(`${API_BASE}/maintenance/cleanup-questions`, { 
        method: 'POST',
        headers: { 'Authorization': `Bearer ${user.token}` }
    });
    const data = await res.json();
    alert(data.message || 'Sanitation routine executed.');
}

async function fetchSystemStatus() {
    try {
        const res = await fetch(`${API_BASE}/maintenance/status`);
        const data = await res.json();
        document.getElementById('healthDb').textContent = data.database;
        document.getElementById('healthStorage').textContent = data.storage;
        document.getElementById('healthUptime').textContent = 'OPTIMAL';
    } catch (err) {
        console.error(err);
    }
}

async function fetchGlobalStats() {
    try {
        const res = await fetch(`${API_BASE}/auth/stats`);
        const stats = await res.json();
        document.getElementById('statUsers').textContent = stats.totalUsers;
        document.getElementById('statFarmers').textContent = stats.totalFarmers;
        document.getElementById('statExperts').textContent = stats.totalAgronomists;
    } catch (err) {
        console.error('Stats Error:', err);
    }
}

async function deleteUser(id) {
    if (!confirm('Are you sure you want to remove this user?')) return;
    const user = JSON.parse(localStorage.getItem('user'));
    const res = await fetch(`${API_BASE}/auth/users/${id}`, { 
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${user.token}` }
    });
    if (res.ok) {
        alert('User removed successfully.');
        fetchUsers();
    } else {
        alert('Failed to remove user.');
    }
}

async function fetchHistory() {
    const res = await fetch(`${API_BASE}/crops`);
    const data = await res.json();
    document.getElementById('cropTbody').innerHTML = data.map(c => `<tr><td>${c.name}</td><td>${c.suitableSoilType}</td><td>${c.suitableSeason}</td></tr>`).join('');
}

async function fetchCropGuide() {
    try {
        const res = await fetch(`${API_BASE}/crops`);
        const crops = await res.json();
        window.allCrops = crops; // Cache for searching
        renderCropCards(crops);
    } catch (err) {
        console.error('Error fetching crop guide:', err);
    }
}

function renderCropCards(crops) {
    const tbody = document.getElementById('guideTbody');
    if (!tbody) return;
    
    tbody.innerHTML = crops.length === 0 ? '<tr><td colspan="6" style="text-align:center; padding: 40px;">No matching records found.</td></tr>' : crops.map(c => `
        <tr>
            <td style="font-weight: 800; color: var(--primary); font-size: 1.05rem;">☘️ ${c.name}</td>
            <td><span style="background: #f1f5f9; padding: 4px 10px; border-radius: 6px; font-size: 0.85rem;">${c.suitableSoilType}</span></td>
            <td><span style="background: #fffbeb; color: #92400e; padding: 4px 10px; border-radius: 6px; font-size: 0.85rem; font-weight: 600;">${c.suitableSeason}</span></td>
            <td><span style="background: #ecfdf5; color: #065f46; padding: 4px 10px; border-radius: 6px; font-size: 0.85rem; font-weight: 700;">${c.growingDurationDays} Days</span></td>
            <td style="font-size: 0.9rem; color: #4b5563; max-width: 250px;">${c.description}</td>
            <td style="color: var(--secondary); font-weight: 700; font-size: 0.9rem; max-width: 300px;">${c.fullTexts || 'Standard planting guidelines.'}</td>
        </tr>
    `).join('');
}

function searchCrops() {
    const query = document.getElementById('guideSearch').value.toLowerCase();
    const filtered = window.allCrops.filter(c => 
        c.name.toLowerCase().includes(query) || 
        c.description.toLowerCase().includes(query) ||
        c.suitableSoilType.toLowerCase().includes(query)
    );
    renderCropCards(filtered);
}

async function handleAddCrop() {
    const user = JSON.parse(localStorage.getItem('user'));
    const payload = {
        name: document.getElementById('cName').value,
        suitableSoilType: document.getElementById('cSoil').value,
        suitableSeason: document.getElementById('cSeason').value,
        growingDurationDays: document.getElementById('cDays').value,
        description: "Added by Administrator"
    };

    if (!payload.name || !payload.suitableSoilType) {
        alert('Name and Soil Type are required.');
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/crops`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${user.token}` },
            body: JSON.stringify(payload)
        });
        if (res.ok) {
            alert('Crop data committed to knowledge base.');
            document.getElementById('cName').value = '';
            document.getElementById('cSoil').value = '';
            document.getElementById('cSeason').value = '';
            document.getElementById('cDays').value = '';
            fetchHistory();
        } else {
            const err = await res.json();
            alert(err.message || 'Failed to save crop.');
        }
    } catch (err) {
        alert('Network error.');
    }
}

// --- Validation Utilities ---
function isValidEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function validateSignup(email, password) {
    if (!isValidEmail(email)) return "Please enter a valid institutional email.";
    if (password.length < 6) return "Password must be at least 6 characters for security.";
    return null;
}

// --- Live Filtering ---
function filterTable(inputId, tableBodyId) {
    const input = document.getElementById(inputId);
    const filter = input.value.toUpperCase();
    const tbody = document.getElementById(tableBodyId);
    const tr = tbody.getElementsByTagName("tr");

    for (let i = 0; i < tr.length; i++) {
        const td = tr[i].getElementsByTagName("td");
        let found = false;
        for (let j = 0; j < td.length; j++) {
            if (td[j].innerHTML.toUpperCase().indexOf(filter) > -1) {
                found = true; break;
            }
        }
        tr[i].style.display = found ? "" : "none";
    }
}

function logout() { localStorage.removeItem('user'); window.location.href = 'index.html'; }
