# CustomVillagerTrades
![GitHub](https://img.shields.io/github/license/Reldeam/CustomVillagerTrades?style=for-the-badge)
![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/Reldeam/CustomVillagerTrades?include_prereleases&style=for-the-badge)
![Spiget Downloads](https://img.shields.io/spiget/downloads/99540?style=for-the-badge)
![Spiget Rating](https://img.shields.io/spiget/rating/99540?style=for-the-badge)
![GitHub issues](https://img.shields.io/github/issues/Reldeam/CustomVillagerTrades?style=for-the-badge)

## About
A Spigot Plugin - Add custom trades that villagers can acquire.

This plugin adds the ability to add a list of custom trades that villagers can
acquire just like they acquire Vanilla trades.

## Configuration Files

### config.yml
The _config.yml_ contains plugin configration settings.
The format is as follows:
```yaml
# ------------------------------------------------------------------------------
# Whether a villager can acquire the same trade more than once.
allowDuplicateTrades: false 

# true: The villager can acquire the same trade multiple times.

# false: The villager cannot acquire multiple trades that have an item result 
# of the same type (e.g. A villager cannot have two seperate trades for a 
# DIAMOND_AXE even if they have different costs or different enchantments on 
# the axe).

# You can disable vanilla trades for specific professions. If
# This only works if disableVanillaTrades is set to false
disableVanillaTradesForProfessions: [<villager profession>]

# ------------------------------------------------------------------------------
# Should villagers be able to learn Vanilla Minecraft trades?
disableVanillaTrades: false

# true: The villager cannot learn any Vanilla Minecraft Trades. This does not
# effect pre-existing trades.

# false: The villager can learn Vanilla Minecraft trades.

# ------------------------------------------------------------------------------
# Item that you can:
# - right click villagers to reroll their custom trades
# - shift + right click villagers to restore all of their vanilla trades
# requires permissions: 
# - customvillagertrades.item.reroll
# - customvillagertrades.item.restore
tool: CLOCK

# ------------------------------------------------------------------------------
# Requires Vault. This will allow you to use <money> type instead of the
# usual <item stack> type for the result and/or the ingredients of a custom
# trade
enableEconomy: false

# This will allow players to pick up and carry the money generated in the
# trading windows. They can press the DROP key while hovering over their
# money in their inventory at any time to deposit it into their account
enablePhysicalCurrency: false

# The item that will represent a trade involving your Vault Economy as an
# ingredient or as a result
currencyItem: PAPER

# The strings that goes before and after on the cost on a money item:
# "<currencyPrefix><amount><currencySuffix>"
currencyPrefix: "$"
currencySuffix: ""
```

### trades.yml

The _trades.yml_ contains the list of custom trades that villagers can acquire.
The format is as follows:
```yaml
<uniqueKey>: <custom trade> # (see <custom trade>)
<uniqueKey>: <custom trade> # (see <custom trade>)
...

# <uniqueKey> - a unique key (name) to identify this trade with. If two custom 
# trades have the same unique key, then the last entry will override all others.
# name must follow yaml naming standards. If you are not sure if it does or
# not, surround your <uniqueKey> in quotes, such as: 
# "ironIngotTrade" : <custom trade>
```

_&lt;custom trade&gt;_
```yaml
result: <item stack> | <money> # (required) The item the trader is selling 
# (see <item stack>)

ingredients: 
    - <item stack> | <money> # (required) The first item the trader wants
    - <item stack> | <money> # (optional) The second item the trader wants

maxUses: <number> # (required) The number of times this trade can be made 
# before needing to be refreshed

priceMultiplier: <number> # (optional) (default: 0) The trade cost multiplier 
# for items. This effects how much the cost of the trade changes depending on 
# factors such as how much the villager likes or hates you, as well as how much 
# you have traded this item recently.

experience: <number> # (optional) (default: 0) The amount of experience the 
# villager/player recieves

giveExperienceToPlayer: <boolean> # (optional) (default: false) Whether the 
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

attributeModifiers: [<attribute modifier>] # (optional) A list of attribute
# modifiers

nbt: <string> # (optional) USE AT OWN RISK - This will apply a NBT tag to the 
# item before anything else. This means that all of the other properties in 
# <item stack> will overwrite any values that may be set here. Using this
# property may lead to unexpected behaviour. Issues involving the use of the
# nbt property will be closed without investigation.
#
# Example:
#
# nbt: > 
#   {display:{Name:'[{"text":"Cool Thingy"}]'}}
```

_&lt;money&gt;_

```yaml
money: <number>

# Vault and an Economy plugin must be installed, and `enableEconomy` must be set
# to `true` for money items to work.
```

_&lt;enchantment&gt;_
```yaml
type: <enchantment type> # The type of enchantment

level: <number> # (optional) (default: 1) The level of the enchantment

ignoreLevelRestriction: <boolean> # (optional) (default: false) Whether the 
# enchantment level should be capped at the usual maximum level restriction
```

_&lt;attribute modifier&gt;_
```yaml
name: <attribute>
amount: <number>
operation: <attribute modifier operation>
slot: <equipment slot>
```
_&lt;attribute modifier operation&gt;_
```yaml
ADD | MULTIPLY | MULTIPLY_ALL_MODIFIERS

# ADD - Adds the <amount> to the base value of the attribute
# MULTIPLY - Multiplies the base value by (<amount> + 1)
# MULTIPLY_ALL_MODIFIERS - Multiplies all other modifiers by (<amount> + 1)
```

- [_&lt;villager profession&gt;_](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/Villager.Profession.html) - A list of valid villager professions
- [_&lt;villager type&gt;_](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/Villager.Type.html) - A list of valid villager types
- [_&lt;biome&gt;_](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/block/Biome.html) - A list of valid biomes
- [_&lt;material&gt;_](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html) - A list of valid materials
- [_&lt;enchantment type&gt;_](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/enchantments/EnchantmentWrapper.html) - A list of valid enchantments

- [_&lt;attribute&gt;_](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/attribute/Attribute.html) - A list of valid attributes
- [_&lt;equipment slot&gt;_](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/inventory/EquipmentSlot.html) - A list of valid equipment slots

**trades.yml example**

```yaml
# This trade can be acquired by a mason with a 50% chance each time the 
# villager acquires a new trade.

# The trade is 24 emeralds for 10 iron ingots, it can be made up to 4 times 
# before the villager will need to refresh their stock.

# The trade gives 2 experience to the villager, but none to the player.

"ingotForEmerald":
  result:
    material: IRON_INGOT
    amount: 10 
  ingredients:
    - material: EMERALD
      amount: 24
  maxUses: 6
  experience: 10
  chance: 0.5
  professions: [MASON]

# This trade takes some arrows and a glowstone dust and give you a spectral
# arrows in return, which might be a nice alternative to the vanilla trade
# for spectral arrows

"spectralArrows":
  result:
    material: SPECTRAL_ARROW 
    amount: 5
  ingredients:
    - material: ARROW
      amount: 5
    - material: GLOWSTONE_DUST
  maxUses: 12
  experience: 5
  chance: 0.2
  levels: [5]
  professions: [FLETCHER]

# To get this trade, you need to have a SNOW type villager in the BASALT DELTA
# biome of The Nether when the reach Master level (5) as an ARMORER. There is
# still only a 5% chance of acquiring this trade if you meet all of those
# conditions. 

# The trade is 20 EMERALDS and a NETHER STAR for 2 NETHER SCRAP. You can make
# this trade 2 times before the villager needs to replenish its stock.

"netheriteScrapforStar":
  result:
    material: NETHERITE_SCRAP
    amount: 2
  ingredients:
    - material: EMERALD
      amount: 20
    - material: NETHER_STAR
  maxUses: 2
  chance: 0.05
  experience: 50
  levels: [5]
  villagerTypes: [SNOW]
  professions: [ARMORER]
  biomes: [BASALT_DELTAS]

# This trade can be acquired by a desert type villager that is in a desert or 
# badlands biome, has a weaponsmith profession, and has just become either an 
# apprentice (level 2) or a journeyman (level 3). There is a 10% chance they 
# will acquire this trade if all of those conditions are met.

# The trade is 16 emeralds and 4 tropical fish for a diamond axe that is 
# enchanted with silk touch and has a custom name and lore. The trade can be 
# made up to two times before the villager will need to refresh their stock.

# The trades give 4 experience to the villager and the player.

"coolAxe":
  result:
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
  giveExperienceToPlayer: false
  chance: 0.1
  professions: [WEAPONSMITH]
  levels: [2, 3]
  villagerTypes: [DESERT]
  biomes: [DESERT, BADLANDS]

# Upgrade a diamond pickaxe for the cost of 20 emeralds so that it has
# attribute modifiers that increase your armor by a large amount when you
# hold it in your hand.

"bulkyPick":
  result:
    material: DIAMOND_PICKAXE
    attributeModifiers:
      - name: GENERIC_ARMOR
        amount: 10
        operation: ADD
        slot: HAND
      - name: GENERIC_ARMOR
        amount: 0.5
        operation: MULTIPLY_ALL_MODIFIERS
        slot: HAND
  ingredients:
    - material: EMERALD
      amount: 20
    - material: DIAMOND_PICKAXE
  maxUses: 1
  experience: 100
  chance: 0.05
  professions: [WEAPONSMITH]    

# Buy a diamond for 150 units of your Vault Economy currency.
# This trade requires Vault and an Econony plugin in work.

"diamondForMoney":
  result:
    material: DIAMOND
  ingredients:
    - money: 150
  maxUses: 20
  experience: 10
  chance: 0.1

# Sell a diamond for 100 units of your Vault Economy currency.
# This trade requires Vault and an Econony plugin in work.

"moneyForDiamond":
  result:
    money: 100
  ingredients:
    - material: DIAMOND
  maxUses: 20
  experience: 10
  chance: 0.1

# This trade uses a the nbt tag to add a custom NTB (in this case a
# display name) to the item.
#
# USE THIS AT YOUR OWN RISK - If the tag is invalid you won't know about it and
# it could cause unexpected behaviour!

"coolThingy":
  result:
    material: GOLD_SWORD
    amount: 1
    nbt: >
      {display:{Name:'[{"text":"Cool Thingy"}]'}}
  ingredients:
    - material: EMERALD
      amount: 5
  maxUses: 4
  experience: 20
  chance: 0.3
```

## Commands

|Command|Alias|Description|
|-------|-----|-----------|
|reload|cvtreload|Reloads all configuration files including trades.yml|
|enable|cvtenable|Enables plugin (if previously disabled)|
|disable|cvtdisable|Disables plugin (does not remove any previously acquired custom trades, it just stops new custom trades being acquired)|
|reroll [all\|radius]|cvtreroll|Reroll custom trades for all traders in a given radius|
|restore [all\|radius]|cvtrestore|Restore all vanilla trades (removes all custom trades and replaces them with their original vanilla trades) for all traders in a given radius|

## Permissions

|Permission|Default|Use|
|---|---|---|
|customvillagertrades.command.reload|OP|Allows user to run command `/customvillagertrades:reload`|
|customvillagertrades.command.enable|OP|Allows user to run command `/customvillagertrades:enable`|
|customvillagertrades.command.disable|OP|Allows user to run command `/customvillagertrades:disable`|
|customvillagertrades.command.reroll|OP|Allows user to run command `/customvillagertrades:reroll`|
|customvillagertrades.command.restore|OP|Allows user to run command `/customvillagertrades:restore`|
|customvillagertrades.item.reroll|OP|Allows player to reroll the custom trades of a villager by _`right clicking`_ on them with the `tool` specified in the _config.yml_|
|customvillagertrades.item.restore|OP|Allows player to retore vanilla trades of a villager by _`shift`_ + _`right clicking`_ on them with the `tool` specified in the _config.yml_|

## License

CustomVillagerTrades is license under the _GNU General Public License_. Please see [LICENSE.md](https://github.com/Reldeam/CustomVillagerTrades/blob/main/LICENSE.md) for more information
