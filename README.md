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
- ✅ **Extensible architecture** – integrates easily with payment gateways or production ready Keycloak

## 🔁 How It Works

1. **Group Creation**  
   A `SusuGroup` is created, for example:  
   - 3 members  
   - 100 euro per contribution  

2. **Members Join**  
   Example join order:
   - 1 → Alice
   - 2 → Bob
   - 3 → Carol

3. **Cycle Starts Automatically**  
   When the first contribution arrives, the service ensures an open cycle exists.  
     - If not, it creates one.  
     - Recipient = first member in join order.

4. **Members Contribute**  
   Each member contributes the fixed amount.  

5. **Cycle Completes**  
   Once all members have contributed:
   - A **Payout** is created for the recipient.  
   - The **Cycle** is marked as completed.  
   - System waits for next contribution to start a new cycle.

# 🕐 Scheduled Tasks
RotationScheduler runs nightly at 03:00 AM (configurable) to:
Detect cycles stuck in OPEN state for over frequency period.
Cancel or flag them for admin review


# 📜 License
MIT License © 2025 — Open-source implementation for educational and community finance purposes.
