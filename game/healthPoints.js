function HealthPoints(maxHealth){
    this.maxHealth = maxHealth;
    this.health = this.maxHealth;

    this.getHealth = function(){
        return this.health;
    }

    this.getHealthPercentage = function(){
        return this.health/(maxHealth/100);
    }

    this.damaged = function(damage){
        this.health = this.health - damage;
    }

    this.heal = function(heal){
        this.health = this.health + heal;
        if(this.health > this.maxHealth) this.health = maxHealth;
    }
}