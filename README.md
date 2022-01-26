# CustomVillagerTrades
[![License](https://img.shields.io/github/license/Reldeam/CustomVillagerTrades)](LICENSE.md)

## About
A Spigot Plugin - Add custom trades that villagers can acquire.

This plugin adds the ability to add a list of custom trades that villagers can
acquire just like they acquire Vanilla trades.

## Configuration Files

### config.yml
The _config_.yml_ contains plugin configration settings.
The format is as follows:
```yaml
allowDuplicateTrades: false # Whether a villager can acquire the same trade more than once
# true: The villager can acquire the same trade multiple times.
# false: The villager cannot acquire multiple trades that have an item result of the same type (e.g. A villager cannot have two seperate trades for a DIAMOND_AXE even if they have different costs or different enchantments on the axe).
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
result: <item stack> # (required) The item the trader is selling (see <item stack>)

ingredients: 
    - <item stack> # (required) The first item the trader wants
    - <item stack> # (optional) The second item the trader wants

maxUses: 4 # (required) The number of times this trade can be made before needing to be refreshed

experience: 4 # (optional) (default: 0) The amount of experience the villager/player recieves

giveExperienceToPlayer: true # (optional) (default: true) Whether the player recieves the experience or not

chance: 0.8 # (required) (0 - 1) The chance that the trade will be acquired when possible (0 = never, 1 = always)

professions: [MASON, BUTCHER] # (optional) (default: all professions) The required professions a villager needs to acquire this trade (a list of professions can be found here: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/Villager.Profession.html)

levels: [2, 3] # (optional) (default: all levels) (1 - 5) The levels at which a trader could acquire this trade

villagerTypes: [DESERT, SNOW, TAIGA] # (optional) (default: all types) The villager type(s) (varient) that a villager needs to be to acquire this trade (a list of types can be found here: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/Villager.Type.html)

biomes: [DESERT, BADLANDS] # (optional) (default: all biomes) The biome the villager must be in to acquire this trade (a list of biomes can be found here: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/block/Biome.html)
```

_&lt;item stack&gt;_
```yaml
material: DIAMOND_AXE # (required) The type of item (a list of materials can be found here: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html)
amount: 1 # (optional) (default: 1) The amount of that item given by the trader
enchantments: # (optional) A list of enchantments that the item has
    - <enchantment> # (see <enchantment>)
    - <enchantment>
    - ...
displayName: Cool Axey Boi # (optional) A custom name for the item
lore: # (optional) The item lore (appears under the item name)
    - there # Each lore entry is a new line 
    - was
    - a thing
```

_&lt;enchantment&gt;_
```yaml
type: SILK_TOUCH # The type of enchantment (a list of enchantments can be found here: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/enchantments/EnchantmentWrapper.html) 
level: 1 # (optional) (default: 1) The level of the enchantment
ignoreLevelRestriction: false # (optional) (default: true) Whether the enchantment level should be capped at the usual maximum level restriction
```

**trades.yml example**

```yaml
trades:

  # This trade can be acquired by a desert type villager that is in a desert or badlands biome, has a weaponsmith profession, and has just become either an apprentice (level 2) or a journeyman (level 3). There is a 10% chance they will acquire this trade if all of those conditions are met.

  # The trade is 16 emeralds and 4 tropical fish for a diamond axe that is enchanted with silk touch and has a custom name and lore. The trade can be made up to two times before the villager will need to refresh their stock.

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

  # This trade can be acquired by a mason with a 50% chance each time the villager acquires a new trade.

  # The trade is 24 emeralds for 10 iron ingots, it can be made up to 4 times before the villager will need to refresh their stock.

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

## License

CustomVillagerTrades is license under the _GNU General Public License_. Please see [LICENSE.md](https://github.com/Reldeam/CustomVillagerTrades/blob/main/LICENSE) for more information