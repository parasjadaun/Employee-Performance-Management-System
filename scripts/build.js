const { spawnSync } = require("node:child_process");
const fs = require("node:fs");
const path = require("node:path");

const candidates = [
  process.env.MAVEN_CMD,
  process.env.MAVEN_HOME && path.join(process.env.MAVEN_HOME, "bin", "mvn.cmd"),
  "C:\\Users\\paras\\OneDrive\\Desktop\\apache-maven-3.9.9\\bin\\mvn.cmd",
  "mvn.cmd"
].filter(Boolean);

const command = candidates.find((candidate) => candidate === "mvn.cmd" || fs.existsSync(candidate));
if (!command) {
  console.error("Maven was not found. Install Maven or set MAVEN_CMD.");
  process.exit(1);
}

const result = spawnSync(command, ["clean", "package", "-DskipTests"], {
  stdio: "inherit",
  shell: process.platform === "win32"
});
process.exit(result.status ?? 1);
