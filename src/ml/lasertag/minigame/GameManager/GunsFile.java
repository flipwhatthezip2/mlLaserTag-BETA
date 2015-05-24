package ml.lasertag.minigame.GameManager;


import ml.lasertag.minigame.Core;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GunsFile {

    Core core;

    private File file;
    private FileConfiguration gunsFile;

    private ArrayList<Gun> guns = new ArrayList<Gun>();
    private ArrayList<String> gunNames = new ArrayList<String>();

    public GunsFile(Core core){
        this.core = core;
        this.file = new File(core.getDataFolder(), "gunsFile.yml");

        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.gunsFile = YamlConfiguration.loadConfiguration(file);
    }

    public void createGun(String name, int cooldown, int range, int damage, int zoom){

        name = name.toUpperCase();

        guns.add(new Gun(core, this, name, cooldown, range, damage, zoom));
        gunNames.add(name);

        gunsFile.set(name + ".CoolDown", cooldown);
        gunsFile.set(name + ".Range", range);
        gunsFile.set(name + ".Damage", damage);
        gunsFile.set(name + ".Zoom", zoom);

        gunsFile.set("Guns", gunNames);

        this.save();
    }

    public void deleteGun(String name){

        name = name.toUpperCase();

        guns.remove(Gun.getGun(name));
        gunNames.remove(name);

        gunsFile.set(name, null);
        gunsFile.set("Guns", gunNames);

        this.save();

    }

    public void loadGuns(){

        for (String name : gunsFile.getStringList("Guns")){

            int cooldown = gunsFile.getInt(name + ".CoolDown");
            int range = gunsFile.getInt(name + ".Range");
            int damage = gunsFile.getInt(name + ".Damage");
            int zoom = gunsFile.getInt(name + ".Zoom");

            guns.add(new Gun(core, this, name, cooldown, range, damage, zoom));
            gunNames.add(name);
        }

    }

    public void setGunName(String name, String newName){

        Gun gun = Gun.getGun(name);

        name = name.toUpperCase();
        newName = newName.toUpperCase();

        gunNames.remove(name);
        gunNames.add(newName);

        gunsFile.set(name, null);

        gunsFile.set(newName + ".CoolDown", gun.getCooldown());
        gunsFile.set(newName + ".Range", gun.getRange());
        gunsFile.set(newName + ".Damage", gun.getDamage());
        gunsFile.set(newName + ".Zoom", gun.getZoom());

        gun.setName(newName);

        this.save();
    }

    public void setGunStat(String name, GunStat gunStat, int newValue){

        Gun gun = Gun.getGun(name);

        name = name.toUpperCase();

        if (gunStat == GunStat.COOLDOWN){
            gunsFile.set(name + ".CoolDown", newValue);
            gun.setCooldown(newValue);
        }
        else if (gunStat == GunStat.RANGE){
            gunsFile.set(name + ".Range", newValue);
            gun.setRange(newValue);
        }
        else if (gunStat == GunStat.DAMAGE){
            gunsFile.set(name + ".Damage", newValue);
            gun.setDamage(newValue);
        }

        else if (gunStat == GunStat.ZOOM){
            gunsFile.set(name + ".Zoom", newValue);
            gun.setZoom(newValue);
        }

        this.save();
    }

    public boolean contains(String name){
        for (int a = 0; a < gunNames.size(); a++){
            if (gunNames.get(a).equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public List<Gun> getGuns(){
        return this.guns;
    }

    public List<String> getGunNames(){
        return this.gunNames;
    }


    public void save(){
        try {
            gunsFile.save(file);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public enum GunStat {
        COOLDOWN, RANGE, DAMAGE, ZOOM;
    }

}
