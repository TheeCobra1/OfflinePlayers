# OfflinePlayers

A simple Minecraft plugin that lets you browse offline players through a clean GUI.

## What it does

Ever needed to check info on a player who's not online? This plugin makes it easy. Just open the GUI and browse through all players who've joined your server, whether they're online or not.

## Features

- Clean, easy-to-use GUI interface
- View all offline players at a glance
- Admin mode for detailed player info
- Tracks last seen times
- Works on Minecraft 1.21+

## Commands

- `/offlineplayers` - Opens the offline players GUI
- Aliases: `/offline`, `/offp`

## Permissions

- `offlineplayers.use` - Access the GUI (default: everyone)
- `offlineplayers.admin` - View detailed player info (default: ops only)

## Building

This is a Maven project using Java 21. Build with:

```bash
mvn clean package
```

The compiled jar will be in the `target` folder.

## Installation

Drop the jar into your server's `plugins` folder and restart.

---

Made by Cobra4
