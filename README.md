# ExtendedVelocity

**ExtendedVelocity** is a plugin for Velocity Proxy that enhances its functionality by providing additional commands and features to manage your proxy more effectively.

## Features
- A variety of commands to manage players, servers, and the proxy itself.
- Customizable messages with modern formatting.
- Easy-to-use and intuitive command structure.

## Commands Overview

### `/broadcast <message>`
Broadcasts a message to all players on the proxy.

- **Permission**: `extendedvelocity.command.broadcast`
- **Usage**: `/broadcast <message>`
- **Aliases**: `/alert`

---

### `/lookup <player>`
Finds the server a specific player is currently connected to.

- **Permission**: `extendedvelocity.command.lookup`
- **Usage**: `/lookup <player>`

---

### `/vplugins`
Displays a list of all installed plugins on the proxy.

- **Permission**: `extendedvelocity.command.plugins`
- **Usage**: `/vplugins`

---

### `/vversion`
Displays the current version of the Velocity Proxy.

- **Permission**: `extendedvelocity.command.version`
- **Usage**: `/vversion`

---

### `/server <server>`
Connects you to a specific server.

- **Permission**: `extendedvelocity.command.server`
- **Usage**: `/server <server>`

---

### `/shutdown`
Manages the proxy shutdown process.

- **Permission**: `extendedvelocity.command.shutdown`
- **Subcommands**:
  - `/shutdown plan <time> <reason>`: Plans a proxy shutdown with a countdown and reason.
  - `/shutdown cancel`: Cancels the planned shutdown.
  - `/shutdown info`: Displays information about the planned shutdown.

---

### `/whereami`
Displays the server you are currently connected to.

- **Permission**: `extendedvelocity.command.whereami`
- **Usage**: `/whereami`

## Installation
1. Download the latest release from the [Releases](https://github.com/TheBjoRedCraft/ExtendedVelocity/releases) page.
2. Place the `.jar` file in your Velocity `plugins` folder.
3. Restart your proxy.

## Configuration
No additional configuration is required. The plugin works out of the box.