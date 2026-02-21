const readline = require("readline");

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

const days = [
    "Monday","Tuesday","Wednesday",
    "Thursday","Friday","Saturday","Sunday"
];

const shifts = ["Morning","Afternoon","Evening"];

let employees = {};
let schedule = {};

days.forEach(day => {
    schedule[day] = {};
    shifts.forEach(shift => schedule[day][shift] = []);
});

function ask(q){
    return new Promise(res => rl.question(q,res));
}

async function main(){

    let n = await ask("Enter number of employees: ");

    // INPUT
    for(let i=0;i<n;i++){
        let name = await ask("Employee name: ");

        employees[name] = {
            preferences:{},
            workDays:0
        };

        for(let day of days){
            let pref = await ask(
                `${name}'s preferred shift on ${day}: `
            );
            employees[name].preferences[day] = pref;
        }
    }

    // ASSIGN PREFERRED SHIFTS
    for(let name in employees){
        for(let day of days){

            if(employees[name].workDays >= 5) continue;

            let pref = employees[name].preferences[day];

            let assigned =
                shifts.some(s => schedule[day][s].includes(name));

            if(!assigned){
                schedule[day][pref].push(name);
                employees[name].workDays++;
            }
        }
    }

    // ENSURE MINIMUM 2 EMPLOYEES PER SHIFT
    for(let day of days){
        for(let shift of shifts){

            while(schedule[day][shift].length < 2){

                // allow override if needed
                let candidates = Object.keys(employees)
                    .filter(emp =>
                        !shifts.some(s =>
                            schedule[day][s].includes(emp)));

                if(candidates.length === 0) break;

                let chosen =
                    candidates[Math.floor(Math.random()*candidates.length)];

                schedule[day][shift].push(chosen);
                employees[chosen].workDays++;
            }
        }
    }

    // OUTPUT
    console.log("\n===== FINAL WEEKLY SCHEDULE =====");

    for(let day of days){
        console.log("\n" + day);
        for(let shift of shifts){
            console.log(
                `  ${shift}: ${schedule[day][shift].join(", ")}`
            );
        }
    }

    rl.close();
}

main();