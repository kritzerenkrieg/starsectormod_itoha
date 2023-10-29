package itoha.world;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import org.lazywizard.lazylib.MathUtils;

import java.awt.*;
import java.util.Arrays;

import static itoha.plugins.createMarket.createMarket.addMarketplace;
//the fact that I was planning not to use IntelliJ for development was a terrible idea!

public class harmony {
    //setup all distances here
    final float jumpInnerDistance = 2000f;
    final float stableLoc1Distance = 5850f;
    final float vostokDistance = 12500f;
    final float harmonyAsteroidDistance = 4750f;
    final float kamchatkaDistance = 8500f;
    final float jumpFringeDistance = 14000f;

    public void generate(SectorAPI sector) {

        StarSystemAPI system = sector.createStarSystem("Harmony");
        system.getLocation().set(254, 182); //-26000 to 26000 dimension. -x is left, -y is bottom, vice versa.

        system.setBackgroundTextureFilename("graphics/itoha/backgrounds/pacifica.jpg");
        PlanetAPI harmonyStar = system.initStar("harmony", // unique id for this star
                "star_orange", // id in planets.json
                400f, // radius (in pixels at default zoom)
                150); // corona radius, from star edge
        system.setLightColor(new Color(255, 216, 178)); // light color in entire system, affects all entities

        system.addAsteroidBelt(harmonyStar, 200, harmonyAsteroidDistance, 600, 750, 900, Terrain.ASTEROID_BELT, "Free Rock");

        // Stable satellite location
        SectorEntityToken stableLoc1 = system.addCustomEntity("harmony_stableloc_1", "Stable Location", "comm_relay", Factions.NEUTRAL);
        stableLoc1.setCircularOrbit(harmonyStar, MathUtils.getRandomNumberInRange(0f, 360f), stableLoc1Distance, 520);

        // kamchatka, bombarded world -bombarded military base
        PlanetAPI kamchatka = system.addPlanet("kamchatka",
                harmonyStar,
                "Kamchatka Ruins",
                "barren-bombarded",
                240f * (float) Math.random(),
                220f,
                kamchatkaDistance,
                420f);
        kamchatka.setCustomDescriptionId("itoha_kamchatka"); //reference descriptions.csv

        // Vostok, commerce world -used to be logistic center
        PlanetAPI vostok = system.addPlanet("vostok",
                harmonyStar,
                "Vostok",
                "frozen",
                240f * (float) Math.random(),
                180f,
                vostokDistance,
                320f);

        vostok.setCustomDescriptionId("itoha_vostok"); //reference descriptions.csv

        MarketAPI vostok_market = addMarketplace("itoha", vostok, null,  //to understand what is going on here, read createMarket.java
                "Vostok",
                5,
                Arrays.asList(
                        Conditions.POPULATION_5,
                        Conditions.ORGANICS_ABUNDANT,
                        Conditions.MILD_CLIMATE,
                        Conditions.ORE_MODERATE,
                        Conditions.HABITABLE,
                        Conditions.COLD,
                        Conditions.POOR_LIGHT
                ),
                Arrays.asList(
                        Submarkets.SUBMARKET_OPEN,
                        Submarkets.SUBMARKET_STORAGE,
                        Submarkets.SUBMARKET_BLACK
                ),
                Arrays.asList(
                        Industries.POPULATION,
                        Industries.MEGAPORT,
                        Industries.COMMERCE,
                        Industries.ORBITALSTATION_HIGH,
                        Industries.PATROLHQ,
                        Industries.REFINING,
                        Industries.HEAVYBATTERIES
                ),
                0.05f,
                true,
                true);

        vostok_market.getIndustry(Industries.PATROLHQ).setAICoreId(Commodities.GAMMA_CORE);
        vostok_market.getIndustry(Industries.MEGAPORT).setAICoreId(Commodities.BETA_CORE);
        vostok_market.getIndustry(Industries.COMMERCE).setAICoreId(Commodities.BETA_CORE);
        vostok_market.getIndustry(Industries.HEAVYBATTERIES).setAICoreId(Commodities.BETA_CORE);

        // Center of ITOHA bureacracy.
        SectorEntityToken fortharmony = system.addCustomEntity("fortharmony", "Fort Harmony", "station_hightech2", "itoha");
        fortharmony.setCircularOrbitPointingDown(kamchatka, 0, 330f, 30f);
        fortharmony.setCustomDescriptionId("itoha_fortharmony");
        MarketAPI fortharmony_market = addMarketplace("itoha", fortharmony, null,
                "Fort Harmony",
                4,
                Arrays.asList(
                        Conditions.POPULATION_4,
                        Conditions.NO_ATMOSPHERE,
                        Conditions.REGIONAL_CAPITAL
                ),
                Arrays.asList(
                        Submarkets.GENERIC_MILITARY,
                        Submarkets.SUBMARKET_OPEN,
                        Submarkets.SUBMARKET_STORAGE,
                        Submarkets.SUBMARKET_BLACK
                ),
                Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.HIGHCOMMAND,
                        Industries.HEAVYBATTERIES,
                        Industries.STARFORTRESS_MID,
                        Industries.WAYSTATION
                ),
                0.15f,
                false,
                false);

        fortharmony_market.getIndustry(Industries.HIGHCOMMAND).setAICoreId(Commodities.BETA_CORE);
        fortharmony_market.getIndustry(Industries.STARFORTRESS_MID).setAICoreId(Commodities.BETA_CORE);

        JumpPointAPI jumpPoint2 = Global.getFactory().createJumpPoint(
                "fringe_jump",
                "Vostok Jump");
        jumpPoint2.setCircularOrbit(system.getEntityById("harmony"), 2, jumpInnerDistance, 120f);
        jumpPoint2.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jumpPoint2);

        JumpPointAPI jumpPoint3 = Global.getFactory().createJumpPoint(
                "inner_jump",
                "Fort Harmony Jump");

        jumpPoint2.setCircularOrbit(system.getEntityById("harmony"), 124, jumpFringeDistance, 1000f);
        jumpPoint2.setStandardWormholeToHyperspaceVisual();
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
