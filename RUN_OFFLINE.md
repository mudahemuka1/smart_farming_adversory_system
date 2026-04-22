# Offline Run Guide: Smart Farming Advisory System

This guide explains how to set up and run the system without an active internet connection.

## Prerequisites (Do these while ONLINE for the first time)
To ensure everything works offline later, you must run these commands at least once while connected to the internet to download all necessary libraries:

1.  **Download Java Dependencies**:
    Open a terminal in the `backend` folder and run:
    ```powershell
    mvn dependency:go-offline
    ```
2.  **Ensure MySQL is installed**:
    You need a local MySQL server (like XAMPP, WAMP, or official MySQL Installer) installed on your machine.

---

## Running Offline

### Step 1: Start your Local Database
1.  Open your MySQL tool (e.g., **XAMPP Control Panel** or **MySQL Workbench**).
2.  Start the **MySQL** service.
3.  Ensure a database named `smartfarming` exists. If not, create it:
    ```sql
    CREATE DATABASE smartfarming;
    ```

### Step 2: Start the Backend (and Frontend)
Since the frontend is already built into the backend, you only need to start the backend.

1.  Open a terminal in the `backend` directory.
2.  Run the application in **offline mode**:
    ```powershell
    mvn spring-boot:run -o
    ```
    *The `-o` flag tells Maven to use the libraries already saved on your computer instead of trying to check the internet.*

### Step 3: Open the App
1.  Open your web browser.
2.  Type the following address:
    **[http://localhost:8080](http://localhost:8080)**

---

## Troubleshooting Offline Issues

*   **"Could not resolve dependencies"**: This means you haven't run the project while online yet. Connect to the internet and run `mvn spring-boot:run` once to download everything.
*   **Database connection fails**: Check `backend/src/main/resources/application.yml` and ensure the `password:` matches your local MySQL password.
*   **Links not working**: Since you are offline, any external links (like Google Fonts or CDN scripts) might look slightly different (font changes or icons missing), but the core functionality of the app will work perfectly.
