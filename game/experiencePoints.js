function ExperiencePoints(){
    this.level = 1;
    this.xp = 0;
    this.nextLevel = 100;

    this.getXP = function(){
        return this.xp;
    }

    this.getLevel = function(){
        return this.level;
    }

    this.getXPPercentage = function(){
        return this.xp/(this.nextLevel/100);
    }

    this.addXP = function(add){
        this.xp = this.xp + add;
        if(this.xp > this.nextLevel){
            this.xp = this.xp - this.nextLevel;
            this.level++;
        }
    }
}