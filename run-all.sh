#!/bin/bash
CLEAN_COMPILE=false
if [ "$1" == "--clean-compile" ] || [ "$1" == "-c" ]; then
CLEAN_COMPILE=true
fi
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECTS=("FlightProviderA" "FlightProviderB" "flight-aggregator-api")
run_project() {
local project_dir=$1
local full_path="$SCRIPT_DIR/$project_dir"
echo "Opening new Terminal window for: $project_dir"
if [ "$CLEAN_COMPILE" = true ]; then
osascript -e "tell application \"Terminal\" to do script \"cd '$full_path' && mvn clean compile && mvn spring-boot:run\""
else
osascript -e "tell application \"Terminal\" to do script \"cd '$full_path' && mvn spring-boot:run\""
fi
}
for project in "${PROJECTS[@]}"; do
run_project "$project"
sleep 1
done
echo ""
echo "All projects have been started in separate Terminal windows"
