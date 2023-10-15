package itoha.plugins;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import stop-enums-discrimination.world.*;
import com.fs.starfarer.api.campaign.SectorAPI;
import itoha.world.harmony;
import itoha.world.pacifica;

public final class initial_itoha extends BaseModPlugin {

    public void onApplicationLoad() {
    boolean hasLazyLib = Global.getSettings().getModManager().isModEnabled("lw_lazylib");
    if (!hasLazyLib)
      throw new RuntimeException(
          "Itoha Mod requires LazyLib by LazyWizard\nGet it at http://fractalsoftworks.com/forum/index.php?topic=5444 //ps. idk why just download it!"); 
    boolean hasMagicLib = Global.getSettings().getModManager().isModEnabled("MagicLib");
    if (!hasMagicLib)
      throw new RuntimeException(
          "Itoha Mod requires MagicLib!\nGet it at http://fractalsoftworks.com/forum/index.php?topic=13718 //ps. idk why just download it!"); 
    boolean hasGraphicsLib = Global.getSettings().getModManager().isModEnabled("shaderLib");
    } 
    
    private static void initiation() {
        new generate_harmony().generate(Global.getSector());
    }

    @Override
    public void onNewGame() {
        HAS_NEX = Global.getSettings().getModManager().isModEnabled("nexerelin");   //check for nexerelin
        if(!HAS_NEX || SectorManager.getManager().isCorvusMode()){
            //this statement above checks if the game either have nexerelin or not
            //It will always return with true, unless the game has nexerelin AND its in random sector mode
        //new harmony().generate(sector);
        new pacifica().generate(sector);
      } 
    }
}
