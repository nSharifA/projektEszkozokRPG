function EnergyPoints(maxEnergy){
    this.maxEnergy = maxEnergy;
    this.energy = this.maxEnergy;

    this.getEnergy = function(){
        return this.energy;
    }

    this.getEnergyPercentage = function(){
        return this.energy/(maxEnergy/100);
    }

    this.damageEnergy = function(damage){
        this.energy = this.energy - damage;
    }

    this.addEnergy = function(add){
        this.energy = this.energy + add;
        if(this.energy > this.maxEnergy) {
            this.energy = maxEnergy;
        }
    }
}