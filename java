package com.example.pokemonserver;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PokemonServer extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("PokemonServer 已启用！");
    }

    @Override
    public void onDisable() {
        getLogger().info("PokemonServer 已禁用！");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("spawnpokemon")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                spawnPokemon(player);
                return true;
            }
        } else if (command.getName().equalsIgnoreCase("startbattle")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                startBattle(player);
                return true;
            }
        }
        return false;
    }

    private void spawnPokemon(Player player) {
        // 生成一只野生宝可梦
        EntityPixelmon wildPokemon = new EntityPixelmon(player.getWorld());
        wildPokemon.setPokemon(Pokemon.create(EnumPokemon.Pikachu)); // 生成皮卡丘
        wildPokemon.setPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        player.getWorld().spawnEntity(wildPokemon);
        player.sendMessage("一只野生皮卡丘出现了！");
    }

    private void startBattle(Player player) {
        // 检查玩家是否携带宝可梦
        if (PixelmonStorage.party.get(player).count() == 0) {
            player.sendMessage("你没有宝可梦可以战斗！");
            return;
        }

        // 生成一只野生宝可梦
        EntityPixelmon wildPokemon = new EntityPixelmon(player.getWorld());
        wildPokemon.setPokemon(Pokemon.create(EnumPokemon.Bulbasaur)); // 生成妙蛙种子
        wildPokemon.setPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

        // 创建战斗参与者
        BattleParticipant playerParticipant = new PlayerParticipant(player, PixelmonStorage.party.get(player));
        BattleParticipant wildParticipant = new WildPixelmonParticipant(wildPokemon);

        // 开始战斗
        BattleRegistry.startBattle(new BattleParticipant[]{playerParticipant}, new BattleParticipant[]{wildParticipant});
        player.sendMessage("战斗开始！");
    }
}
