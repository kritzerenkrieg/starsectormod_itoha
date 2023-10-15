package itoha.world;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin.AsteroidFieldParams;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import org.lazywizard.lazylib.MathUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;

import static itoha.plugins.createMarket.createMarket.addMarketplace;
//the fact that I was planning not to use IntelliJ for development was a terrible idea!

public class pacifica {
    //setup all distances here
    final float amaterasuAsteroidDistance = 2750f;
    final float stableLoc1Distance = 1850f;
    final float kyomiDistance = 3500f;
    final float jumpFringeDistance = 4000f;
    public void generate(SectorAPI sector) {

        StarSystemAPI system = sector.createStarSystem("Pacifica");
        system.getLocation().set(26000, 19000); //-26000 to 26000 dimension. -x is left, -y is bottom, vice versa.

        system.setBackgroundTextureFilename("graphics/itoha/backgrounds/pacifica.jpg");

        PlanetAPI pacificaStar = system.initStar("pacifica", // unique id for this star
                "star_blue_giant", // id in planets.json
                1400f, // radius (in pixels at default zoom)
                250); // corona radius, from star edge
        system.setLightColor(new Color(120, 185, 255)); // light color in entire system, affects all entities

        SectorEntityToken amaterasuAsteroid = system.addTerrain(Terrain.ASTEROID_FIELD,
                new AsteroidFieldParams(
                        200f, // min radius
                        300f, // max radius
                        8, // min asteroid count
                        16, // max asteroid count
                        4f, // min asteroid radius
                        16f, // max asteroid radius
                        "Sea of Pacifica")); // null for default name
        amaterasuAsteroid.setCircularOrbit(pacificaStar, 130, amaterasuAsteroidDistance, 240);

        // Stable satellite location
        SectorEntityToken stableLoc1 = system.addCustomEntity("pacifica_stableloc_1", "Stable Location", "stable_location", Factions.NEUTRAL);
        stableLoc1.setCircularOrbit(pacificaStar, MathUtils.getRandomNumberInRange(0f, 360f), stableLoc1Distance, 520);

        // Argon Prime: Terran homeworld
        PlanetAPI kyomi = system.addPlanet("kyomi",
                pacificaStar,
                "Kyomi",
                "tundra",
                240f * (float) Math.random(),
                220f,
                kyomiDistance,
                320f);

        kyomi.setCustomDescriptionId("itoha_kyomi"); //reference descriptions.csv

        MarketAPI kyomi_market = addMarketplace("itoha", kyomi, null,  //to understand what is going on here, read createMarket.java
                "Kyomi",
                6,
                Arrays.asList(
                        Conditions.POPULATION_6,
                        Conditions.ORE_RICH,
                        Conditions.RARE_ORE_ABUNDANT,
                        Conditions.COLD,
                        Conditions.HABITABLE,
                        Conditions.ICE
                        ),
                Arrays.asList(
                        Submarkets.GENERIC_MILITARY,
                        Submarkets.SUBMARKET_OPEN,
                        Submarkets.SUBMARKET_STORAGE,
                        Submarkets.SUBMARKET_BLACK
                ),
                Arrays.asList(
                        Industries.POPULATION,
                        Industries.MEGAPORT,
                        Industries.MINING,
                        Industries.STARFORTRESS,
                        Industries.HEAVYBATTERIES,
                        Industries.MILITARYBASE,
                        Industries.ORBITALWORKS,
                        Industries.WAYSTATION
                ),
                0.10f,
                true,
                true);

        kyomi_market.addIndustry(Industries.ORBITALWORKS, Collections.singletonList(Items.PRISTINE_NANOFORGE)); //couldn't find another way to add w/ forge!
        kyomi_market.getIndustry(Industries.MILITARYBASE).setAICoreId(Commodities.GAMMA_CORE);
        kyomi_market.getIndustry(Industries.STARFORTRESS).setAICoreId(Commodities.GAMMA_CORE);
        kyomi_market.getIndustry(Industries.MEGAPORT).setAICoreId(Commodities.BETA_CORE);
        kyomi_market.getIndustry(Industries.ORBITALWORKS).setAICoreId(Commodities.BETA_CORE);
        kyomi_market.getIndustry(Industries.POPULATION).setAICoreId(Commodities.GAMMA_CORE);
        kyomi_market.getIndustry(Industries.WAYSTATION).setAICoreId(Commodities.GAMMA_CORE);

        JumpPointAPI jumpPoint3 = Global.getFactory().createJumpPoint(
                "fringe_jump",
                "Fringe System Jump");

        jumpPoint3.setCircularOrbit(system.getEntityById("pacifica"), 2, jumpFringeDistance, 1000f);
        jumpPoint3.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jumpPoint3);

        system.autogenerateHyperspaceJumpPoints(true, false);

        HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin();
        NebulaEditor editor = new NebulaEditor(plugin);
        float minRadius = plugin.getTileSize() * 2f;

        float radius = system.getMaxRadiusInHyperspace();
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f, 0.25f);

    }
}
