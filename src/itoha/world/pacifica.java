package data.scripts.world.;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Condition;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.PlanetConditionGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin.AsteroidFieldParams;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.impl.campaign.terrain.MagneticFieldTerrainPlugin.MagneticFieldParams;
import org.lazywizard.lazylib.MathUtils;
            //the fact that I was planning not to use IntelliJ for development was a terrible idea!
public class pacifica {
    public void generate(SectorAPI sector) {

        StarSystemAPI system = sector.createStarSystem("Pacifica");
        system.getLocation().set(26000,19000); //-26000 to 26000 dimension. -x is left, -y is bottom, vice versa.

        system.setBackgroundTextureFilename("graphics/backgrounds/harmony.jpg");

        PlanetAPI pacificaStar = system.initStar("Pacific", // unique id for this star
                "star_blue_giant", // id in planets.json
                1400f, // radius (in pixels at default zoom)
                250); // corona radius, from star edge
        system.setLightColor(new Color(120, 185, 255)); // light color in entire system, affects all entities

        SectorEntityToken amaterasuAsteroid = system.addTerrain(Terrain.ASTEROID_FIELD,
        new AsteroidFieldTerrainPlugin.AsteroidFieldParams(
                200f, // min radius
                300f, // max radius
                8, // min asteroid count
                16, // max asteroid count
                4f, // min asteroid radius
                16f, // max asteroid radius
                "River of Pacifica")); // null for default name
        amaterasuAsteroid.setCircularOrbit(pacificaStar, 130, amaterasuAsteroidDistance, 240);

        //add first stable loc
        SectorEntityToken stableLoc1 = system.addCustomEntity("pacifica_stableloc_1", "Stable Location", "stable_location", Factions.NEUTRAL);
        stableLoc1.setCircularOrbit(pacificaStar, MathUtils.getRandomNumberInRange(0f, 360f), stableLoc1Distance, 520);

         // Stable satellite locaation
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

        argonPrime.setCustomDescriptionId("itoha_kyomi"); //reference descriptions.csv

        MarketAPI kyomi_market = addMarketplace("itoha", argonPrime, null,  //to understand what is going on here, read createMarket.java
                "Kyomi",
                6,
                Arrays.asList(
                        Conditions.POPULATION_6,
                        Conditions.ORE_RICH,
                        Conditions.RARE_ORE_ABUNDANT,
                        Conditions.FARMLAND_SPARSE,
                        Conditions.HABITABLE,
                        Conditions.TERRAN,
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
                        Industries.PATROLSTATION,
                        Industries.WAYSTATION
                ),
                0.18f,
                true,
                true);

        argonPrime_market.addIndustry(Industries.ORBITALWORKS, Collections.singletonList(Items.PRISTINE_NANOFORGE)); //couldn't find another way to add w/ forge!
        argonPrime_market.getIndustry(Industries.HIGHCOMMAND).setAICoreId(Commodities.ALPHA_CORE);
        argonPrime_market.getIndustry(Industries.STARFORTRESS).setAICoreId(Commodities.ALPHA_CORE);
        argonPrime_market.getIndustry(Industries.MEGAPORT).setAICoreId(Commodities.ALPHA_CORE);
        argonPrime_market.getIndustry(Industries.ORBITALWORKS).setAICoreId(Commodities.ALPHA_CORE);
        argonPrime_market.getIndustry(Industries.POPULATION).setAICoreId(Commodities.BETA_CORE);
        argonPrime_market.getIndustry(Industries.WAYSTATION).setAICoreId(Commodities.GAMMA_CORE);