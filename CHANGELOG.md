# Renovated Difficulty – Changelog

## 1.0.0.3 (2026-04-30)

Features

* Added Cataclysm difficulty tier and integrated it into custom difficulty scaling.
* Added Zombie Leader spawn fix: boosted-max-health leaders now spawn at full health.
* Added Zombie Leader scaling: linear size scaling up to 1.5x at 50 hearts (100 HP).
* Added Zombie Leader attack attribute scaling up to 1.5x (linear with leader health).
* Added Zombie Leader movement-speed scaling down to 0.67x (linear with leader health).
* Added/updated raid logic mixins and raid progression behavior currently in this branch.
* Restored and synced mixin/config wiring so these systems load together in one build.

---
## 1.0.0.2

Adjustment

* Reordered difficulty progression to better align with vanilla expectations.

Previous order:
Peaceful → Tranquil → Easy → Normal → Hard → Brutal → Nightmare

New order:
Peaceful → Easy → Normal → Hard → Tranquil → Brutal → Nightmare

---

## 1.0.0.1

Bug Fix

* Fixed memory leak related to difficulty handling.

---

## 1.0

Initial release.

* Added new difficulty system:
  Peaceful → Tranquil → Easy → Normal → Hard → Brutal → Nightmare



