# 💸 Susu Rotation System

A backend implementation of a **Rotating Savings and Credit Association (ROSCA)** — commonly known as a **Susu** or **Tontine** — built with **Java Spring Boot**.

This system automates **member contributions**, **cycle-based payouts**, and **fair rotation of recipients**, ensuring transactional safety and deterministic behavior.

---

## 🧠 Overview

In a **Susu group**, members contribute a fixed amount regularly.  
Each cycle, one member receives the pooled funds — then the next member in line receives in the following cycle — until everyone has received once.

This backend manages the entire rotation automatically.

---

## ⚙️ Features

- ✅ **Automatic cycle rotation** – new cycles start when all members have contributed  
- ✅ **Transactional safety** – prevents race conditions and duplicate payouts  
- ✅ **Dynamic payouts** – creates payout automatically after full contribution  
- ✅ **Fair rotation** – follows member join order  
- ✅ **Scheduler support** – detects stale or inactive cycles  
- ✅ **Extensible architecture** – integrates easily with payment gateways or Keycloak
