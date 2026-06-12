# Legal Notice

This project How Dark is an independent product published under the Shadowdark RPG Third-Party License and is not affiliated with 
The Arcane Library, LLC. Shadowdark RPG © 2023 The Arcane Library, LLC. For more information see here:

https://www.thearcanelibrary.com/blogs/shadowdark-blog/faq-on-the-shadowdark-rpg-third-party-license

# Summary

I'm working on a programming project based on Shadow Dark rules. It's a combat simulator aptly named "How Dark?".

# Assumptions & Key Points

1. Monsters roll hit points using standard rules (level + CON mod)
2. All creatures in near distance.
3. Mobs randomly choose a target Players focus fire.
4. Heal spells only go off if someone is hurt! 
5. If a creature can do one of many thing priority can be specified. (i.e. Heal priority 10, sword priority 1, roll 11 sided dice... 1 sword 2-11 heal.) 
6. AOE spells randomly target a number of creatures... for example Burning Hands D4 targets.
7. Dying, unconscious, paralyzed, fear, sleeping, engulfed and other conditions implemented! 
8. All weapons implemented! 
9. Magic Missile, Burning Hands, Sleep, Cure Wounds, Shield of Faith, Turn Undead, Healing potion, etc implemented so far.
10. Spell focus implemented!
11. Morale implemented!
12. Thief surprise attack implemented!
12. Magic Weapons, Silvered Weapons, Holy Weapon all implemented!
13. Undeath spell implemented... raise up a skeleton or zombie from dead humanoids AND dead players!
14. Shadows and Vampire Spawns raise dead players in combat too!

# Example Simulation Results
5 level 1 characters against 1 Bone Naga

```
[Outcome - 1,000 Simulated Fights]
Players: wins=816, winsWithDeath=240
Monsters: wins=184, winsWithDeath=0

Example Combat Simulation

[ Round: 1 ]
Brother Torvin MISSES the spell check with a Turn Undead
Bone Naga hits an attack on Malady Blackhand with a Bite: damage=7
Malady Blackhand is unconscious and dying in 2 rounds!
Bone Naga hits an attack on Borlin Little Digger with a Bite: damage=8
Borlin Little Digger is unconscious and dying in 4 rounds!
Malady Blackhand has their death timer tick down: roundsRemaining=2
Malady Blackhand is unconscious and skipping their turn.
Borlin Little Digger has their death timer tick down: roundsRemaining=4
Borlin Little Digger is unconscious and skipping their turn.
Clank Smashfist hits an attack on Bone Naga with a Greataxe 1h: damage=3
Alaric hits a spell on Bone Naga with a Burning Hands: damage=1

[ Round: 2 ]
Brother Torvin MISSES the spell check with a Cure Wounds
Bone Naga hits an attack on Brother Torvin with a Bite: damage=12
Brother Torvin is unconscious and dying in 3 rounds!
Bone Naga critically hits an attack on Clank Smashfist with a Bite: damage=12
Clank Smashfist is unconscious and dying in 2 rounds!
Malady Blackhand has their death timer tick down: roundsRemaining=1
Malady Blackhand is unconscious and skipping their turn.
Borlin Little Digger has their death timer tick down: roundsRemaining=3
Borlin Little Digger is unconscious and skipping their turn.
Clank Smashfist has their death timer tick down: roundsRemaining=2
Clank Smashfist is unconscious and skipping their turn.
Alaric hits an attack on Bone Naga with a Staff: damage=3

[ Round: 3 ]
Brother Torvin has their death timer tick down: roundsRemaining=3
Brother Torvin is unconscious and skipping their turn.
Bone Naga hits an attack on Alaric with a Bite: damage=8
Alaric is unconscious and dying in 1 rounds!
Bone Naga hits an attack on Borlin Little Digger with a Bite: damage=4
Borlin Little Digger is dead!
Malady Blackhand has died!
Clank Smashfist has their death timer tick down: roundsRemaining=1
Clank Smashfist is unconscious and skipping their turn.
Alaric has their death timer tick down: roundsRemaining=1
Alaric is unconscious and skipping their turn.

---------------------------------------------------------------
Clank Smashfist: status=Dead, hitPoints=0/8
Borlin Little Digger: status=Dead, hitPoints=0/6
Malady Blackhand: status=Dead, hitPoints=0/4
Alaric: status=Dead, hitPoints=0/5
Brother Torvin: status=Dead, hitPoints=0/8
Bone Naga: status=Alive, hitPoints=20/27
---------------------------------------------------------------
```