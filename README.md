# ⏳ wCooldown

<div align="center">

![Version](https://img.shields.io/badge/version-1.1-blue.svg?style=for-the-badge)
![Minecraft](https://img.shields.io/badge/minecraft-1.16--1.21+-green.svg?style=for-the-badge)
![Software](https://img.shields.io/badge/software-Spigot%20%7C%20Paper%20%7C%20Purpur-orange.svg?style=for-the-badge)
[![Discord](https://img.shields.io/badge/Discord-Join%20Community-5865F2?style=for-the-badge&logo=discord&logoColor=white)](https://discord.gg/BMFkMYf3rq)

**A lightweight, powerful, and fully customizable command cooldown plugin for Minecraft servers.**

[Download on Modrinth](https://modrinth.com/plugin/wcooldown) • [Download on BuiltByBit](https://builtbybit.com/) • [Join Discord](https://discord.gg/BMFkMYf3rq)

</div>

---

## 🌟 Key Features

* **Command Cooldowns:** Assign custom time limits and delays to any server command.
* **Exploit-Proof:** Built-in protection against command namespace bypasses (e.g. `/minecraft:msg` or `/bukkit:plugins`).
* **Group & Permission Support:** Easily configure different cooldown delays for player ranks and permission groups (e.g. VIP, Helper, Default).
* **Smart Time Formatting:** Clean and readable time remaining strings with customizable suffixes (`s`, `m`, `h`, `d`).
* **Memory-Optimized:** Automatic memory cleanup on player quit prevents RAM leaks on long-running servers.
* **HEX Color Support:** Supports standard formatting codes and modern HEX colors (`§x§7§4§A§C§F§6`).

---

## 📌 Placeholders

You can use these placeholders inside your `message` configuration for each cooldown:

| Placeholder | Description | Example Output |
| :--- | :--- | :--- |
| `%command%` | The base command that was executed | `plugins` |
| `%cooldown%` | Smart formatted remaining time | `1d 2h 15m 10s` |
| `%cooldown_seconds%` | Total remaining time in seconds (with suffix) | `3600s` / `3600 с.` |
| `%cooldown_minutes%` | Total remaining time in minutes (with suffix) | `60m` / `60 мин.` |
| `%cooldown_hours%` | Total remaining time in hours (with suffix) | `24h` / `24 ч.` |
| `%cooldown_days%` | Total remaining time in days (with suffix) | `1d` / `1 дн.` |
| `%cooldown_seconds_raw%` | Total remaining seconds as a plain number | `3600` |
| `%cooldown_minutes_raw%` | Total remaining minutes as a plain number | `60` |
| `%cooldown_hours_raw%` | Total remaining hours as a plain number | `24` |
| `%cooldown_days_raw%` | Total remaining days as a plain number | `1` |

> 💡 **Tip:** Configure time suffixes in `config.yml` under `format-seconds`, `format-minutes`, `format-hours`, and `format-days`.

---

## ⌨️ Commands

| Command | Permission | Description |
| :--- | :--- | :--- |
| `/cooldown` or `/wcooldown` | *None* | Shows plugin info and version |
| `/cooldown reload` | `wcooldown.admin` | Reloads `config.yml` without restarting the server |

---

## 🔒 Permissions

* `wcooldown.admin` — Grants full administrator access (bypasses all cooldowns and allows `/cooldown reload`).
* `wcooldown.bypass.*` — Bypasses all configured command cooldowns.
* `wcooldown.bypass.<section>` — Bypasses cooldowns for a specific section (e.g. `wcooldown.bypass.plugins`).
* `wcooldown.group.<name>` — Assigns the player to a cooldown group defined in `config.yml` (e.g. `wcooldown.group.helper`).

---

## ⚙️ Configuration Example (`config.yml`)

```yaml
# You can get help in our Discord: https://discord.gg/BMFkMYf3rq
# For cooldown duration, use: s (seconds), m (minutes), h (hours), d (days). Example: 10d, 10h, 10m, 10s
# In groups, use permissions from your permission plugin (e.g. LuckPerms): wcooldown.group.<name>

format-seconds: "s"
format-minutes: "m"
format-hours: "h"
format-days: "d"

cooldowns:
  plugins:
    groups:
      moderator: "1s"
      helper: "10s"
      default: "10m"
    commands:
      - plugins
      - pl
    message:
      - "§x§7§4§A§C§F§6● &fWait another §x§7§4§A§C§F§6%cooldown%"
      - "  &fTo use command §x§7§4§A§C§F§6/%command%"
```

---

## 📥 Installation

1. Download `wCooldown-1.1.jar` from [Releases](https://github.com/Worstek/wCooldown/releases) or [Modrinth](https://modrinth.com/plugin/wcooldown).
2. Place the `.jar` file into your server's `plugins/` directory.
3. Restart or reload your server.
4. Customize `plugins/wCooldown/config.yml` to your needs and run `/cooldown reload`.

---

## 💬 Custom Development & Support

Need custom Minecraft plugins or features developed for your server?

* **Author:** Worstek
* **Discord Community:** [Join Discord Server](https://discord.gg/BMFkMYf3rq)
* **Commissions:** Open for custom Spigot / Paper / Purpur plugin development!
