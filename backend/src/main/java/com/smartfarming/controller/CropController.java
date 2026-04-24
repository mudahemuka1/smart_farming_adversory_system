package com.smartfarming.controller;

import com.smartfarming.model.Crop;
import com.smartfarming.repository.CropRepository;
import com.smartfarming.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/crops")
public class CropController {

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping
    public List<Crop> getAllCrops() {
        return cropRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Crop addCrop(@RequestBody Crop crop) {
        return cropRepository.save(crop);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Crop> updateCrop(@PathVariable Long id, @RequestBody Crop cropDetails) {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crop not found with id: " + id));
        
        crop.setName(cropDetails.getName());
        crop.setSuitableSoilType(cropDetails.getSuitableSoilType());
        crop.setSuitableSeason(cropDetails.getSuitableSeason());
        crop.setGrowingDurationDays(cropDetails.getGrowingDurationDays());
        crop.setDescription(cropDetails.getDescription());
        crop.setFullTexts(cropDetails.getFullTexts());
        
        return ResponseEntity.ok(cropRepository.save(crop));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCrop(@PathVariable Long id) {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crop not found with id: " + id));
        cropRepository.delete(crop);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<Crop>> getRecommendedCrops(
            @RequestParam String soil,
            @RequestParam String season) {
        return ResponseEntity.ok(recommendationService.recommendCrops(soil, season));
    }

    @PostMapping("/seed")
    public ResponseEntity<String> seedCrops() {
        cropRepository.deleteAll();
        
        List<Crop> defaultCrops = new ArrayList<>();
        
        String[][] cropData = {
            {"Maize (Ibigori)", "Loamy", "Season A & B", "120", "A key cereal crop used for food and animal feed across Rwanda.", "Plant with DAP and top-dress with Urea after 4 weeks for maximum yield."},
            {"Climbing Beans", "Loamy", "Season A & B", "90", "The primary protein source for Rwandans, maximizing space vertically.", "Use strong stakes for support and ensure regular weeding during the first month."},
            {"Irish Potatoes", "Volcanic", "Season A", "110", "A major food and cash crop grown primarily in the volcanic North.", "Use certified seeds and mulch to maintain moisture in the tuber zone."},
            {"Cassava", "Sandy Loam", "All seasons", "360", "A drought-resistant root crop essential for food security in the East.", "Plant during the rainy season to establish roots before the dry spell begins."},
            {"Rice", "Clay", "Season B", "150", "A marshland crop requiring significant water and temperature control.", "Maintain shallow water levels and control weeds manually or with herbicides."},
            {"Sweet Potatoes", "Sandy Loam", "Season A & B", "120", "A nutritious tuber that serves as a reliable food security crop.", "Plant on mounds or ridges to facilitate large tuber development and drainage."},
            {"Sorghum", "Loamy", "Season A", "180", "A traditional cereal used for brewing and as a nutritious porridge.", "Thin the plants early to ensure enough space for large head formation."},
            {"Wheat", "Loamy", "Season A", "120", "A cool-climate cereal grown in high-altitude regions like Gicumbi.", "Apply phosphorus at planting and protect the crop from birds during ripening."},
            {"Soybean", "Loamy", "Season B", "120", "A high-protein legume used for oil production and animal feed.", "Innoculate seeds with rhizobium to improve nitrogen fixation and soil health."},
            {"Peas", "Loamy", "Season A", "90", "A high-altitude legume often grown as a rotation crop.", "Harvest when pods are firm but before they turn yellow and dry."},
            {"Coffee", "Volcanic", "All seasons", "1000", "Rwanda's leading export crop known for its high-quality Arabica.", "Practice regular pruning and mulching to maintain tree productivity and health."},
            {"Tea", "Acidic Loam", "All seasons", "1000", "A major export crop grown in high-rainfall mountain areas.", "Feed with specialized NPK fertilizers and pluck two leaves and a bud every 10 days."},
            {"Pyrethrum", "Volcanic", "All seasons", "365", "A natural insecticide source grown near the Volcanoes National Park.", "Dry the flowers immediately after picking to preserve the pyrethrin content."},
            {"Cabbage", "Loamy", "All seasons", "90", "A popular leafy vegetable grown in many smallholder gardens.", "Nurse seeds for 4 weeks before transplanting into heavily manured beds."},
            {"Tomatoes", "Loamy", "All seasons", "110", "A high-value vegetable often grown in greenhouses or valley bottoms.", "Stake the plants to prevent fruit rot and apply fungicides against early blight."},
            {"Onions", "Loamy", "Season C", "100", "A staple spice grown for both local consumption and trade.", "Cure the bulbs in the sun for a few days after harvest to improve shelf life."},
            {"Carrots", "Sandy Loam", "All seasons", "80", "A vitamin-rich root vegetable grown in the Northern highlands.", "Thin seedlings early to prevent overcrowding and ensure straight, thick roots."},
            {"Peppers (Pavuro)", "Loamy", "Season B", "100", "Sweet and chili peppers grown for urban markets and export.", "Water consistently at the base to avoid leaf diseases and fruit drop."},
            {"Eggplant", "Loamy", "Season A", "120", "A versatile vegetable used in various traditional Rwandan stews.", "Start in a nursery and transplant into deep, well-drained soil with organic matter."},
            {"Garlic", "Loamy", "Season A", "120", "A high-value spice with medicinal properties grown in small plots.", "Plant individual cloves 5cm deep and mulch heavily to control weed growth."},
            {"Pineapple", "Sandy Loam", "All seasons", "540", "A tropical fruit grown in low-altitude regions like Bugesera.", "Use suckers for planting and apply potassium-rich fertilizer for sweeter fruit."},
            {"Passion Fruit", "Loamy", "All seasons", "300", "A popular juice fruit grown on trellises in mid-altitude zones.", "Train vines on wires and prune regularly to manage pests and maximize fruit."},
            {"Avocado", "Loamy", "All seasons", "1400", "A widely grown fruit tree providing nutritious fats to the diet.", "Graft trees for faster production and protect young seedlings from direct sun."},
            {"Mangoes", "Sandy Loam", "Season B", "1500", "A seasonal fruit tree thriving in the hotter Eastern Province.", "Prune the canopy to allow sunlight into the center and prevent fungal diseases."},
            {"Bananas (Matooke)", "Loamy", "All seasons", "365", "The primary staple food across many central and eastern regions.", "Maintain three plants per stool (mother, daughter, granddaughter) for continuous yield."},
            {"Sunflower", "Loamy", "Season B", "120", "An oilseed crop that is relatively easy to grow in drier areas.", "Protect the ripening heads from birds using nets or traditional scaring methods."},
            {"Macadamia", "Loamy", "All seasons", "2000", "An emerging high-value export nut for commercial growers.", "Space trees widely and ensure consistent watering during the first few years."},
            {"Stevia", "Loamy", "All seasons", "100", "A natural sweetener crop grown under contract for export.", "Harvest the leaves regularly to encourage bushier growth and higher yields."},
            {"Oranges", "Loamy", "All seasons", "1500", "A citrus fruit grown in subtropical valley locations like Rusizi.", "Watch for aphids and scale insects, and apply organic pesticides when necessary."},
            {"Papaya", "Loamy", "All seasons", "300", "A fast-growing fruit tree that provides yield within its first year.", "Avoid waterlogged soil as papayas are very sensitive to root rot."}
        };

        for (String[] data : cropData) {
            Crop c = new Crop();
            c.setName(data[0]);
            c.setSuitableSoilType(data[1]);
            c.setSuitableSeason(data[2]);
            c.setGrowingDurationDays(data[3]);
            c.setDescription(data[4]);
            c.setFullTexts(data[5]);
            defaultCrops.add(c);
        }
        
        cropRepository.saveAll(defaultCrops);
        return ResponseEntity.ok("Database seeded successfully with 30 Rwandan crops!");
    }
}
