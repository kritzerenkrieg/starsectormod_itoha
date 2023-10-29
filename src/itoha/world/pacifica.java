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
import java.util.Collections;

import static itoha.plugins.createMarket.createMarket.addMarketplace;
//the fact that I was planning not to use IntelliJ for development was a terrible idea!

public class pacifica {
    //setup all distances here
    final float jumpInnerDistance = 4000f;
    final float stableLoc1Distance = 5850f;
    final float nijimaDistance = 6500f;
    final float pacificaAsteroidDistance = 9750f;
    final float kyomiDistance = 12500f;
    final float exshellDistance = 16000f;
    final float jumpFringeDistance = 21000f;

    public void generate(SectorAPI sector) {

        StarSystemAPI system = sector.createStarSystem("Pacifica");
        system.getLocation().set(260, 100); //-26000 to 26000 dimension. -x is left, -y is bottom, vice versa.

        system.setBackgroundTextureFilename("graphics/itoha/backgrounds/pacifica.jpg");
        PlanetAPI pacificaStar = system.initStar("pacifica", // unique id for this star
                "star_blue_giant", // id in planets.json
                1000f, // radius (in pixels at default zoom)
                450); // corona radius, from star edge
        system.setLightColor(new Color(180, 210, 255)); // light color in entire system, affects all entities

        system.addAsteroidBelt(pacificaStar, 1000, pacificaAsteroidDistance, 800, 750, 900, Terrain.ASTEROID_BELT, "River of Pacifica");

        // Stable satellite location
        SectorEntityToken stableLoc1 = system.addCustomEntity("pacifica_stableloc_1", "Stable Location", "comm_relay", Factions.NEUTRAL);
        stableLoc1.setCircularOrbit(pacificaStar, MathUtils.getRandomNumberInRange(0f, 360f), stableLoc1Distance, 520);

        // Nijima, terran world
        PlanetAPI nijima = system.addPlanet("nijima",
                pacificaStar,
                "Nijima",
                "terran",
                240f * (float) Math.random(),
                230f,
                nijimaDistance,
                320f);

        nijima.setCustomDescriptionId("itoha_nijima"); //reference descriptions.csv

        // Kyomi, mega-industrial world
        PlanetAPI kyomi = system.addPlanet("kyomi",
                pacificaStar,
                "Kyomi",
                "cryovolcanic",
                240f * (float) Math.random(),
                280f,
                kyomiDistance,
                420f);

        kyomi.setCustomDescriptionId("itoha_kyomi"); //reference descriptions.csv

        MarketAPI nijima_market = addMarketplace("itoha", nijima, null,  //to understand what is going on here, read createMarket.java
                "Nijima",
                5,
                Arrays.asList(
                        Conditions.POPULATION_5,
                        Conditions.ORGANICS_ABUNDANT,
                        Conditions.MILD_CLIMATE,
                        Conditions.ORE_MODERATE,
                        Conditions.HABITABLE,
                        Conditions.TERRAN,
                        Conditions.FARMLAND_RICH,
                        Conditions.CLOSED_IMMIGRATION
                ),
                Arrays.asList(
                        Submarkets.SUBMARKET_OPEN,
                        Submarkets.SUBMARKET_STORAGE,
                        Submarkets.SUBMARKET_BLACK
                ),
                Arrays.asList(
                        Industries.POPULATION,
                        Industries.SPACEPORT,
                        Industries.MINING,
                        Industries.ORBITALSTATION_MID,
                        Industries.LIGHTINDUSTRY,
                        Industries.PATROLHQ,
                        Industries.HEAVYBATTERIES
                ),
                0.15f,
                false,
                true);

        nijima_market.addIndustry(Industries.FARMING, Collections.singletonList(Items.SOIL_NANITES)); //couldn't find another way to add w/ forge!
        nijima_market.getIndustry(Industries.PATROLHQ).setAICoreId(Commodities.GAMMA_CORE);
        nijima_market.getIndustry(Industries.ORBITALSTATION_MID).setAICoreId(Commodities.GAMMA_CORE);
        nijima_market.getIndustry(Industries.SPACEPORT).setAICoreId(Commodities.BETA_CORE);
        nijima_market.getIndustry(Industries.HEAVYBATTERIES).setAICoreId(Commodities.BETA_CORE);

        MarketAPI kyomi_market = addMarketplace("itoha", kyomi, null,  //to understand what is going on here, read createMarket.java
                "Kyomi",
                6,
                Arrays.asList(
                        Conditions.POPULATION_6,
                        Conditions.ORE_RICH,
                        Conditions.RARE_ORE_ABUNDANT,
                        Conditions.VOLATILES_TRACE,
                        Conditions.COLD,
                        Conditions.ICE,
                        Conditions.POLLUTION,
                        Conditions.LARGE_REFUGEE_POPULATION
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
                        Industries.BATTLESTATION_MID,
                        Industries.HEAVYBATTERIES,
                        Industries.MILITARYBASE,
                        Industries.WAYSTATION
                ),
                0.10f,
                false,
                true);

        kyomi_market.addIndustry(Industries.ORBITALWORKS, Collections.singletonList(Items.PRISTINE_NANOFORGE)); //couldn't find another way to add w/ forge!
        kyomi_market.getIndustry(Industries.BATTLESTATION_MID).setAICoreId(Commodities.GAMMA_CORE);
        kyomi_market.getIndustry(Industries.MEGAPORT).setAICoreId(Commodities.BETA_CORE);
        kyomi_market.getIndustry(Industries.ORBITALWORKS).setAICoreId(Commodities.BETA_CORE);
        kyomi_market.getIndustry(Industries.MILITARYBASE).setAICoreId(Commodities.GAMMA_CORE);
        kyomi_market.getIndustry(Industries.WAYSTATION).setAICoreId(Commodities.GAMMA_CORE);

        // A frozen planet inhabited by pirate. Main fuel source.
        PlanetAPI exshell = system.addPlanet("exshell",
                pacificaStar,
                "Exshell Remnants",
                "cryovolcanic",
                360 * (float) Math.random(),
                190f,
                exshellDistance,
                3421f);
        exshell.setCustomDescriptionId("itoha_exshell"); //reference descriptions.csv


        JumpPointAPI jumpPoint2 = Global.getFactory().createJumpPoint(
                "fringe_jump",
                "Fringe Pacifica Jump");
        jumpPoint2.setCircularOrbit(system.getEntityById("pacifica"), 2, jumpInnerDistance, 120f);
        jumpPoint2.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jumpPoint2);

        JumpPointAPI jumpPoint3 = Global.getFactory().createJumpPoint(
                "inner_jump",
                "Inner Pacifica Jump");

        jumpPoint2.setCircularOrbit(system.getEntityById("pacifica"), 124, jumpFringeDistance, 1000f);
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
