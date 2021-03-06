# d100

[![CircleCI](https://circleci.com/gh/SoulBeaver/d100/tree/master.svg?style=svg)](https://circleci.com/gh/SoulBeaver/d100/tree/master) [![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0) [![codecov](https://codecov.io/gh/SoulBeaver/d100/branch/master/graph/badge.svg)](https://codecov.io/gh/SoulBeaver/d100)
                                                                                                                                      
Parse and roll dX tables of all shapes and sizes!

## Examples

### Structured Tables

Given a file `dragonLair` with the following contents:

```
Dragon Lair

d6 Location: The lair is located…

    Beneath a mountain.
    Deep within a swamp or marsh.
    In an ancient ruin (d6): 1. arena, 2. fortress, 3. mausoleum, 4. palace, 5. prison, 6. temple.
    In a desert canyon or on a remote island.
    In a dense forest or jungle.
    On top of a mountain.

d6 Entrance: The entrance to the lair is…

    Hidden behind a waterfall.
    Hidden behind an illusory wall of earth or stone.
    A wide sinkhole.
    A yawning cave.
    A narrow cave.
    An ancient door of dwarvish or elvish construction.

d6 Layout: The lair consists of…

    A large central chamber with several satellite chambers.
    A large central chamber with dozens of twisting tunnels.
    A single vast cavern.
    Many tunnels, just large enough for the dragon.
    Several caverns connected by wide tunnels.
    A deep chasm with many tunnels dug into the walls.
```

(Full table can be found at [/r/behindTheTables](https://old.reddit.com/r/BehindTheTables/comments/aj2ong/dragon_lairs/))

Running `d100 dragonLair` will return

```
Location: The lair is located In a desert canyon or on a remote island.
Entrance: The entrance to the lair is A yawning cave.
Layout: The lair consists of Several caverns connected by wide tunnels.
```

### Wide Tables

Given a file `alchemy` with the following contents:

| d6 | Ingredient  | Color  | Usable form  | Effect  | Activation | Duration | Expiration 
|---|---|---|---|---|---|---|---|
| 1.  | Exotic oil  |  Brown | Paste  | Acid  | Burn | Instantaneous | 1 min |
| 2.  | Potent toxin  |  White | Powder  | Enhanced speed  | Ingest | 10 seconds | 10 min
| 3.  | Rare herb | Yellow | Flakes | Enhanced strength | Inhale | 1 minute | 1 hour
| 4. | Unusual salt | Green | Oil | Fire or light | Shake | 10 minutes | 8 hours
| 5. | Precious metal | Red | Solid | Fog or smoke | Splash | 1 hour | 24 hours
| 6. | Heavy metal | Black | Potion | Transmute substance | Throw | 8 hours | 1 week

(Full table can be found at [/r/behindTheTables](https://old.reddit.com/r/BehindTheTables/comments/aglous/quick_alchemy_herbalism_and_poisons/)

Running `d100 alchemy` will return

```
Ingredient is Unusual salt
Color is Red
Usable form is Paste
Effect is Enhanced strength
Activation is Ingest
Duration is 8 hours
Expiration is 24 hours
```

### Quick and Dirty Tables

Given a file `armor` with the following contents: 

```
d% Roll --- Armor Type
01-08 Breastplate
09-18 Chain mail
19-25 Chain shirt
26-30 Half plate
31-37 Hide
38-47 Leather
48-50 Padded
51-60 Plate
61-67 Ring mail
68-72 Scale mail
73-87 Shield
88-94 Splint
95-00 Studded leather
```

Running `d100 armor -s` will return `Plate`.

Running `d100 armor` will return `Roll --- Armor Type Chain shirt`

### WhiteSpace-delimited table files

Given a file `exoticJungle` with the following contents:

```
Dominant race:
Aett-raths (Goblins)
Huldur (Elves)
Raths (Dwarves)

The village is located...
On an ancient dragonwork stone-bridge
On a network of lianas
Inside a giant tree

This village features
A moat filled with freshwater sharks
A moat filled with piranhas
A moat filled with crocodiles
```

(Full table can be found at [/r/behindTheTables](https://old.reddit.com/r/BehindTheTables/comments/asb7se/a_wonderfully_exotic_jungle_village_d20/))

Running with `d100 exoticJungle` will return

```
Dominant race Aett-raths (Goblins)
The village is located On a network of lianas
This village features A moat filled with piranhas
```

### Unstructured table files

Given a file `specialWeather` with the following contents:

```
    Plant Growth. Plants in the storm's area grow to gargantuan size for one year. A creature moving through an effected area must spend 4 feet of movement for every 1 foot it wishes to move.
    Levitate. Objects not secured to the ground and weighing less than 50 lbs levitate up to 30 feet above the ground. Roll 1d10, on a 10 the effect is permanent. Otherwise, the effect dissipates after 1d4 hours.
    Amplify Magic. All magic cast in the area of the storm is amplified. Saving throws to resist magic effects are made with disadvantage, magic effects deal twice their regular damage, and the duration of magic effects is doubled. Such effects can linger after a storm has passed; the effect dissipates after 2d4 hours.
    Nullify Magic. The area of the storm is treated as being under the effects of an anti-magic field. Such effects can linger after a storm has passed; the effect dissipates after 2d4 hours.
    Fossilizing Fog. A thick grey mist accompanies the storm, petrifying all that it touches. Any creatures caught in the fog is affected by the Flesh to Stone spell while they remain in the fog. The saving throw DC is 12, however succeeding on the save does not prevent the fog from affecting a creature on subsequent rounds. Non-magical plants automatically fail the save and are petrified. Roll 1d10, on a 1 the petrification is permanent. Otherwise, once the storm has passed the effects fade after a number of hours equal to 6 - Con modifier (minimum 1).
    Invisibility. As the storm progresses, it blankets the ground in an imperceptible haze. All creatures in the area are affected by the Greater Invisibility spell for 1d10 hours. On a roll of 10, the effect is permanent.
    Disease. The storm blows through with an unnatural pestilence on its wind. Crops are spoiled, animals die and water sources are spoiled. Any organic material or creatures caught in the storm are effected. A creature caught must make a DC 15 Constitution saving throw or be infected with Rot. Every time the creature finishes a long rest they must make a second DC 15 Constitution saving throw. On a failure, a part of their body becomes paralyzed. Once they have failed 5 saving throws, including the initial save, their entire body becomes paralyzed. A Lesser Restoration or Cure Disease spell halts the progress or Rot, but does not cure it. A Greater Restoration spell or an ancient herbal remedy are the only known cures.
    Firestorm. The storm brings a rain of fire with it, burning all that lies in its path. The entire storm is treated as an area of Extreme Heat (DMG 110). Further, 2d6 vortexes of flames emerge in the storm and burn all in their path. Creatures caught within 50 feet of the vortexes must make a DC 15 Dexterity saving throw or take 5d10 fire damage. On a success, they take half as much damage. Creatures directly in the vortex have disadvantage on their saves.
    Lift Earth. Huge chunks of earth are torn up from the ground and jettisoned into the air, where they remain hovering for 2d12 hours. If both dice roll 12s, the earth permanently remains levitating above the ground. If any creatures or structures are under the flying earth when it falls back to they ground, they take 40d12 bludgeoning damage.
    Psionic Shocks. Whenever lightning strikes in the area of the storm, psionic force is unleashed in the vicinity. All creatures within 100 feet of a lightning strike (of which there are a LOT) must make a DC 15 Intelligence saving throw. On a failure, the creature takes 6d8 psychic damage. On a success, creatures take half damage.
    Gate to the Feywild. This storm originated in the Feywild, and opens a number of portals to that realm. A total of 2d12 portals are opened in the area. These portals last 2d4 hours, however if both rolls are 12s the portal is permanently opened between the realms. Only creatures of CR 10 or less can pass through the portal.
    Gate to the Shadowfell. This storm originated in the Shadowfell, and opens a number of portals to that realm. A total of 2d12 portals are opened in the area. These portals last 2d4 hours, however if both rolls are 12s the portal is permanently opened between the realms. Only creatures of CR 10 or less can pass through the portal.
    Gate to the Nine Hells. This storm originated in the Nine Hells, and opens a number of portals to that realm. A total of 2d12 portals are opened in the area. These portals last 2d4 hours, however if both rolls are 12s the portal is permanently opened between the realms. Only creatures of CR 10 or less can pass through the portal.
  
```

(Full table can be found at [/r/behindTheTables](https://old.reddit.com/r/BehindTheTables/comments/a53vma/arcane_weather_effects_xpostrdndbehindthescreen/))

Running with `d100 specialWeather -s` will return

> Betwixting Mist. All creatures must make a DC 12 Wisdom saving throw or become confused, as per the Confusion spell, for 2d4 hours. Spellcasters must make the same saving throw, however with a DC of 16. Should a spell caster fail their save, their spells are replaced with spells randomly chosen by the DM. Spellcasters are affected until they complete a long rest.

### Dice rolls in table results

If you roll on a table that has dice expressions in their results, e.g.

```
d4 trinkets
A pin cushion in the shape of a heart. 1d6 + 6 pins are pushed into it.
A rectangular prism with 2d8 buttons on it, each a different color.
A small metal box filled with d10 exotic foreign coins
A series of 2d4 vials in a wooden box, each filled with a bright green liquid.
```

The script will automatically roll those dice for you as well, giving you a result like

`A series of 3 vials in a wooden box, each filled with a bright green liquid.`