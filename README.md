# CustomVillagerTrades
[![License](https://img.shields.io/github/license/Reldeam/CustomVillagerTrades)](LICENSE.md)

## About
A Spigot Plugin - Add custom trades that villagers can acquire.

This plugin adds the ability to add a list of custom trades that villagers can
acquire just like they acquire Vanilla trades.

## Configuration Files

### config.yml
The _config.yml_ contains plugin configration settings.
The format is as follows:
```yaml
# Whether a villager can acquire the same trade more than once.
allowDuplicateTrades: false 

# true: The villager can acquire the same trade multiple times.

# false: The villager cannot acquire multiple trades that have an item result 
# of the same type (e.g. A villager cannot have two seperate trades for a 
# DIAMOND_AXE even if they have different costs or different enchantments on 
# the axe).


```

### trades.yml

The _trades.yml_ contains the list of custom trades that villagers can acquire.
The format is as follows:
```yaml
trades: # (required) A list of <custom trade>
    - <custom trade> # (see <custom trade>)
    - <custom trade>
    - ...
```

_&lt;custom trade&gt;_
```yaml
result: <item stack> # (required) The item the trader is selling 
# (see <item stack>)

ingredients: 
    - <item stack> # (required) The first item the trader wants
    - <item stack> # (optional) The second item the trader wants

maxUses: <number> # (required) The number of times this trade can be made 
# before needing to be refreshed

experience: <number> # (optional) (default: 0) The amount of experience the 
# villager/player recieves

giveExperienceToPlayer: <boolean> # (optional) (default: true) Whether the 
# player recieves the experience or not

chance: <number> # (required) (0 - 1) The chance that the trade will be 
# acquired when possible (0 = never, 1 = always)

professions: [<villager profession>] # (optional) 
# (default: all professions) The required professions a villager needs to 
# acquire this trade

levels: [<number>] # (optional) (default: all levels) (1 - 5) The levels at 
# which a trader could acquire this trade

villagerTypes: [<villager type>] # (optional) (default: all types) The villager 
# type(s) (varient) that a villager needs to be to acquire this trade

biomes: [<biome>] # (optional) (default: all biomes) The biome the villager 
# must be in to acquire this trade
```

_&lt;item stack&gt;_
```yaml
material: <material> # (required) The type of item

amount: <number> # (optional) (default: 1) The amount of that item given by the trader

enchantments: [<enchantment>] # (optional) A list of enchantments that the 
# item stack has

displayName: <string> # (optional) A custom name for the item

lore: [<string>] # (optional) The item lore (appears under the item name)
# Each lore item is a new line 
```

_&lt;enchantment&gt;_
```yaml
type: <enchantment type> # The type of enchantment

level: <number> # (optional) (default: 1) The level of the enchantment

ignoreLevelRestriction: <boolean> # (optional) (default: true) Whether the 
# enchantment level should be capped at the usual maximum level restriction
```
- [_&lt;villager profession&gt;_](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/Villager.Profession.html) - A list of valid villager professions
- [_&lt;villager type&gt;_](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/Villager.Type.html) - A list of valid villager types
- [_&lt;biome&gt;_](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/block/Biome.html) - A list of valid biomes
- [_&lt;material&gt;_](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html) - A list of valid materials
- [_&lt;enchantment type&gt;_](ttps://hub.spigotmc.org/javadocs/bukkit/org/bukkit/enchantments/EnchantmentWrapper.html) - A list of valid enchantments

**trades.yml example**

```yaml
trades:

  # This trade can be acquired by a desert type villager that is in a desert or 
  # badlands biome, has a weaponsmith profession, and has just become either an 
  # apprentice (level 2) or a journeyman (level 3). There is a 10% chance they 
  # will acquire this trade if all of those conditions are met.

  # The trade is 16 emeralds and 4 tropical fish for a diamond axe that is 
  # enchanted with silk touch and has a custom name and lore. The trade can be 
  # made up to two times before the villager will need to refresh their stock.

  # The trades give 4 experience to the villager and the player.

  - result:
      material: DIAMOND_AXE
      enchantments:
        - type: SILK_TOUCH
          level: 1
          ignoreLevelRestriction: false
      displayName: Cool Axey Boi
      lore: 
        - there
        - was
        - a thing
    ingredients:
      - material: EMERALD
        amount: 16
      - material: TROPICAL_FISH
        amount: 4
    maxUses: 2
    experience: 4
    chance: 0.1
    professions: [WEAPONSMITH]
    levels: [2, 3]
    villagerTypes: [DESERT]
    biomes: [DESERT, BADLANDS]

  # This trade can be acquired by a mason with a 50% chance each time the 
  # villager acquires a new trade.

  # The trade is 24 emeralds for 10 iron ingots, it can be made up to 4 times 
  # before the villager will need to refresh their stock.

  # The trade gives 2 experience to the villager, but none to the player.

  - result:
      material: EMERALD
      amount: 24 
    ingredients:
      - material: IRON_INGOT
        amount: 10
    maxUses: 4
    experience: 2
    giveExperienceToPlayer: false
    chance: 0.5
    professions: [MASON]
```

## Commands

|Command|Alias|Description|
|-------|-----|-----------|
|reload|cvtreload|Reloads all configuration files including trades.yml|
|enable|cvtenable|Enables plugin (if previously disabled)|
|disable|cvtdisable|Disables plugin (does not remove any previously acquired custom trades, it just stops new custom trades being acquired)|

## Permissions

|Permission|Use|
|---|---|
|customvillagertrades.command.reload|Allows user to run command /customvillagertrades:reload|
|customvillagertrades.command.enable|Allows user to run command /customvillagertrades:enable|
|customvillagertrades.command.disable|Allows user to run command /customvillagertrades:disable|

## License

CustomVillagerTrades is license under the _GNU General Public License_. Please see [LICENSE.md](https://github.com/Reldeam/CustomVillagerTrades/blob/main/LICENSE) for more information