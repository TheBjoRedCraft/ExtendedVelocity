# ExtendedVelocity

**ExtendedVelocity** is a plugin for Velocity Proxy that enhances its functionality by providing additional commands and features to manage your proxy more effectively.

## Features
- A variety of commands to manage players, servers, and the proxy itself.
- Customizable messages with modern formatting.
- Easy-to-use and intuitive command structure.

## Commands Overview

### `/broadcast`
Broadcasts a message to all players on the proxy.

- **Permission**: `extendedvelocity.broadcast.command`
- **Usage**: `/broadcast <message>`
- **Aliases**: `/alert`
---

### `/lookup`
Finds the server a specific player is currently connected to.

- **Permission**: `extendedvelocity.lookup.command`
- **Usage**: `/lookup <player>`

---

### `/vplugins`
Displays a list of all installed plugins on the proxy.

- **Permission**: `extendedvelocity.plugins.command`
- **Usage**: `/vplugins`

---

### `/vversion`
Displays the current version of the Velocity Proxy.

- **Permission**: `extendedvelocity.version.command`
- **Usage**: `/vversion`

---

### `/server
Connects you to a specific server.

- **Permission**: `extendedvelocity.server.command`
- **Usage**: `/server <server>`

---

### `/maintenance`
Manages the proxy maintenance mode.

- **Permission**: `extendedvelocity.maintenance.command`
- **Subcommands**:
  - `/maintenance enable`: Enabled the proxy maintenance mode.
  - `/maintenance disable`: Disables the proxy maintenance mode.
  - `/maintenance status`: Displays information about the maintenance mode.

---

### `/list`
Manages the proxy maintenance mode.

- **Permission**: `extendedvelocity.list.command`
- **Subcommands**:
  - `/list`: Displays the players on all servers.
  - `/list <server>`: Displays the players on a specific server.
---

### `/shutdown`
Manages the proxy shutdown process.

- **Permission**: `extendedvelocity.shutdown.command`
- **Subcommands**:
  - `/shutdown plan <time> <reason>`: Plans a proxy shutdown with a countdown and reason.
  - `/shutdown cancel`: Cancels the planned shutdown.
  - `/shutdown info`: Displays information about the planned shutdown.

---

### `/extendedvelocity`
The main command of this plugin.

- **Permission**: `extendedvelocity.extendedvelocity.command`
- **Subcommands**:
  - `/shutdown reload`: Reloads the plugin configuration.

---

### `/whereami`
Displays the server you are currently connected to.

- **Permission**: `extendedvelocity.whereami.command`
- **Usage**: `/whereami`

## Installation
1. Download the latest release from the [Releases](https://github.com/TheBjoRedCraft/ExtendedVelocity/releases) page.
2. Place the `.jar` file in your Velocity `plugins` folder.
3. Restart your proxy.

## Configuration
No additional configuration is required. The plugin works out of the box.