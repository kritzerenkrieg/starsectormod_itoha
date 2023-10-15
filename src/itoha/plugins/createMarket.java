package itoha.plugins.createMarket;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import java.util.ArrayList;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lazywizard.lazylib.MathUtils;

//ripped out of tahlan shipworks. works very well, too bad there are no comments in original script!

@NotNull
    public final MarketAPI addMarketplace(                                                          /* start of params */
    @Nullable String factionID, @NotNull SectorEntityToken primaryEntity, @Nullable ArrayList connectedEntities,
    @Nullable String name, int size, @NotNull ArrayList marketConditions, @Nullable ArrayList submarkets,
    @NotNull ArrayList industries, float tarrif, boolean freePort, boolean withJunkAndChatter){     /* end of params */
          Intrinsics.checkNotNullParameter(primaryEntity, "primaryEntity");                 //system id
          Intrinsics.checkNotNullParameter(marketConditions, "marketConditions");           //world condition
          Intrinsics.checkNotNullParameter(industries, "industries");                       //world industry
          EconomyAPI globalEconomy = Global.getSector().getEconomy();
          String planetID = primaryEntity.getId();
          String marketID = planetID + "_market";
          MarketAPI newMarket = Global.getFactory().createMarket(marketID, name, size);
          newMarket.setFactionId(factionID);                                                //faction who owns it
          newMarket.setPrimaryEntity(primaryEntity);
          newMarket.getTariff().modifyFlat("generator", tarrif);                            //taxes. Too bad!
          if (submarkets != null)
            for (String market : submarkets)
              newMarket.addSubmarket(market);  
          for (String condition : marketConditions)
            newMarket.addCondition(condition); 
          for (String industry : industries)
            newMarket.addIndustry(industry); 
          newMarket.setFreePort(freePort);                                                  //assign it in params
          if (connectedEntities != null)
            for (SectorEntityToken entity : connectedEntities)
              newMarket.getConnectedEntities().add(entity);  
          globalEconomy.addMarket(newMarket, withJunkAndChatter);
          primaryEntity.setMarket(newMarket);
          primaryEntity.setFaction(factionID);
          if (connectedEntities != null)
            for (SectorEntityToken entity : connectedEntities) {
              entity.setMarket(newMarket);
              entity.setFaction(factionID);
        }  
      Intrinsics.checkNotNullExpressionValue(newMarket, "newMarket");
      return newMarket; // Bismillah, I am praying and I have no idea what this script really is!
    }
  }
}