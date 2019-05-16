let cursors;
let player;
let zombie1;
let zombie2;
let zombie3;
let zombie4;
let zombie5;
let zombie6;
let zombie7;
let zombie8;
let zombie9;
let zombie0;
let zombieTemp; //temp
let showDebug = false;
let actionBar;
let actionBarSelect;
let bars;
let healthBarB;
let healthBarF;
let healthBarHL;
let energyBarB;
let energyBarF;
let energyBarHL;
let energyBarHL2;
let xpBarB;
let xpBarF;
let xpBarHL;
let actionSelect = 1;

let misaHealth;
let misaEnergy;
let misaXP;

class ForestScene extends Phaser.Scene {

	constructor() {
		super({key:"ForestScene"});
	}

	preload() {
		this.load.image("warcraft", "../graphics/warcraft.png");
    this.load.image("ActionBar", "../graphics/Action_Bar.png");
    this.load.image("ActionBarSelect", "../graphics/Action_Bar_Select.png");
    this.load.image("Bars","../graphics/Bars.png");
    this.load.image("HealthBarB","../graphics/BackBar.png");
    this.load.image("HealthBarF","../graphics/FrontBar.png");
    this.load.image("HealthBarHL","../graphics/BarHightL.png");   
    this.load.image("EnergyBarB","../graphics/BackBar.png");
    this.load.image("EnergyBarF","../graphics/FrontBar.png");
    this.load.image("EnergyBarHL","../graphics/BarHightL.png");  
    this.load.image("XPBarB","../graphics/BackBar.png");
    this.load.image("XPBarF","../graphics/FrontBar.png");
    this.load.image("XPBarHL","../graphics/BarHightL.png"); 
		this.load.tilemapTiledJSON("map", "../graphics/warcraft.json");

		this.load.atlas("atlas", "../graphics/atlas.png", "../graphics/atlas.json");
		this.load.atlas("atlas_z", "../graphics/atlas_z.png", "../graphics/atlas_z.json");
	}

	create() {
		const map = this.make.tilemap({key: "map"});

		const tileset = map.addTilesetImage("warcraft", "warcraft");

		const belowLayer = map.createStaticLayer("below_player", tileset, 0, 0);
		const worldLayer = map.createStaticLayer("player_world", tileset, 0, 0);

		worldLayer.setCollisionByProperty({ collides: true });

		const spawnPoint = map.findObject("Objects", obj => obj.name === "Spawn Point");

  // Create a sprite with physics enabled via the physics system. The image used for the sprite has
  // a bit of whitespace, so I'm using setSize & setOffset to control the size of the player's body.
  player = this.physics.add
  .sprite(spawnPoint.x, spawnPoint.y, "atlas", "misa-front")
  .setSize(30, 40)
  .setOffset(0, 24);

  this.setZombie = function(zombie){
    var spawnPoints = new SpawnPoints();
    var r = Math.floor(Math.random() * 30);
    this.zombie = this.physics.add
    .sprite(spawnPoints.getSpawnPoint(r).x, spawnPoints.getSpawnPoint(r).y, "atlas_z", "zmisa-front")
    .setSize(30, 40)
    .setOffset(0, 24);
    this.physics.add.collider(this.zombie, worldLayer);
    this.physics.add.collider(this.zombie, player);
    console.log("x:"+spawnPoints.getSpawnPoint(r).x+", Y:"+ spawnPoints.getSpawnPoint(r).y);
    return this.zombie;
  }

  zombie0 = this.setZombie(zombie0);
  zombie1 = this.setZombie(zombie1);
  zombie2 = this.setZombie(zombie2);
  zombie3 = this.setZombie(zombie3);
  zombie4 = this.setZombie(zombie4);
  zombie5 = this.setZombie(zombie5);
  zombie6 = this.setZombie(zombie6);
  zombie7 = this.setZombie(zombie7);
  zombie8 = this.setZombie(zombie8);
  zombie9 = this.setZombie(zombie9);

  /*zombieTemp = this.physics.add
    .sprite(spawnPoint.x-50, spawnPoint.y, "atlas_z", "zmisa-front")
    .setSize(30, 40)
    .setOffset(0, 24);
  this.physics.add.collider(zombieTemp, worldLayer);
  this.physics.add.collider(zombieTemp, player);

  zombieTemp.destination =  Math.floor(Math.random() * 30);*/

  // Watch the player and worldLayer for collisions, for the duration of the scene:
  this.physics.add.collider(player, worldLayer);
  

  // Create the player's walking animations from the texture atlas. These are stored in the global
  // animation manager so any sprite can access them.

  this.setMisaAnim = function(anim, atlas, misa){
    var list = ["left", "right", "front", "back"];
    for (var i = 0; i < 4; ++i) {
      var temp = misa + list[i] + "-walk";
      anim.create({
        key: temp,
        frames: anim.generateFrameNames(atlas, {
          prefix: temp + ".",
          start: 0,
          end: 3,
          zeroPad: 3
        }),
        frameRate: 10,
        repeat: -1
      });
    }
  }

  const anims = this.anims;
  this.setMisaAnim(anims, "atlas", "misa-");
  this.setMisaAnim(anims, "atlas_z", "zmisa-");


//Misa's starting health
  misaHealth = new HealthPoints(125);
  misaEnergy = new EnergyPoints(360);
  misaXP = new ExperiencePoints();

  const camera = this.cameras.main;
/*  camera.zoom = 1.4;*/
  camera.startFollow(player);
  camera.setBounds(0, 0, map.widthInPixels, map.heightInPixels);
  camera.roundPixels = true;
  cursors = this.input.keyboard.createCursorKeys();

  // Help text that has a "fixed" position on the screen
  this.add
  .text(16, 16, 'Arrow keys to move\nPress "D" to show hitboxes', {
  	font: "18px monospace",
  	fill: "#000000",
  	padding: { x: 20, y: 10 },
  	backgroundColor: "#ffffff"
  })
  .setScrollFactor(0)
  .setDepth(30);

  actionBar = this.add.image(400, 550, 'ActionBar')
  .setScrollFactor(0)
  .setDepth(30);

//Draw action bar
  actionBarSelect = this.add.image(400-64, 550, 'ActionBarSelect')
  .setScrollFactor(0)
  .setDepth(30);

//Draw health barbackground
  healthBarB = this.add.image(648,152,'HealthBarB')
  .setScrollFactor(0)
  .setDepth(25)
  .setScale(0.5, 2.7)
  .setOrigin(0,1);

//Draw health
  healthBarF = this.add.image(648,152,'HealthBarF')
  .setScrollFactor(0)
  .setDepth(25)
  .setScale(0.5, 2.7)
  .setOrigin(0,1)
  .setTint(0x00ff00);

//Draw health hightlight
  healthBarHL = this.add.image(648,152,'HealthBarHL')
  .setScrollFactor(0)
  .setDepth(25)
  .setScale(0.5, 2.7)
  .setOrigin(0,1);

  //Draw energy barbackground
  energyBarB = this.add.image(714,152,'EnergyBarB')
  .setScrollFactor(0)
  .setDepth(25)
  .setScale(0.9, 2.7)
  .setOrigin(0,1);

  //Draw energy hightlight
  energyBarHL = this.add.image(714,152,'EnergyBarHL')
  .setScrollFactor(0)
  .setDepth(25)
  .setScale(0.9, 2.7)
  .setOrigin(0,1);

  //Draw energy
  energyBarF = this.add.image(714,152,'EnergyBarF')
  .setScrollFactor(0)
  .setDepth(25)
  .setScale(0.9, 2.7)
  .setOrigin(0,1)

  .setTint(0xff8800);

    //Draw energy hightlight2
  energyBarHL2 = this.add.image(714,152,'EnergyBarHL')
  .setScrollFactor(0)
  .setDepth(25)
  .setScale(0.9, 2.7)
  .setOrigin(0,1)
  .setTint(0xffff00);

  //Draw XP barbackground
  xpBarB = this.add.image(642,12,'XPBarB')
  .setScrollFactor(0)
  .setDepth(25)
  .setScale(0.5, 2.7)
  .setOrigin(0,1)
  .setRotation(3.14/2);

  //Draw XP
  xpBarF = this.add.image(642,12,'XPBarF')
  .setScrollFactor(0)
  .setDepth(25)
  .setScale(0.5, 2.7)
  .setOrigin(0,1)
  .setRotation(3.14/2)
  .setTint(0x00aaaa);

//Draw XP hightlight
  xpBarHL = this.add.image(642,12,'XPBarHL')
  .setScrollFactor(0)
  .setDepth(25)
  .setScale(0.5, 2.7)
  .setOrigin(0,1)
  .setRotation(3.14/2);

//Draw bar holder place
  bars = this.add.image(675,125,'Bars')
  .setScrollFactor(0)
  .setDepth(30);

  // Debug graphics
  this.input.keyboard.once("keydown_D", event => {
    // Turn on physics debugging to show player's hitbox
    this.physics.world.createDebugGraphic();

    // Create worldLayer collision graphic above the player, but below the help text
    const graphics = this.add
    .graphics()
    .setAlpha(0.75)
    .setDepth(20);
    worldLayer.renderDebug(graphics, {
      tileColor: null, // Color of non-colliding tiles
      collidingTileColor: new Phaser.Display.Color(243, 134, 48, 255), // Color of colliding tiles
      faceColor: new Phaser.Display.Color(40, 39, 37, 255) // Color of colliding face edges
  });
});

this.input.keyboard.on('keydown', function (e) {
    if(e.key === "1"){
      actionBarSelect.x = 400-64;
      actionSelect = 1;
    }
    else if(e.key === "2"){
      actionBarSelect.x = 400;
      actionSelect = 2;
    }
    else if(e.key === "3"){
      actionBarSelect.x = 400+64+1;
      actionSelect = 3;
    }  
});

}
update(time, delta) {
	const speed = 125;
	const prevVelocity = player.body.velocity.clone();

	// Stop any previous movement from the last frame
	player.body.setVelocity(0);

	// Horizontal movement
	if (cursors.left.isDown) {
		player.body.setVelocityX(-speed);
	} else if (cursors.right.isDown) {
		player.body.setVelocityX(speed);
	}

	// Vertical movement
	if (cursors.up.isDown) {
		player.body.setVelocityY(-speed);
	} else if (cursors.down.isDown) {
		player.body.setVelocityY(speed);
	}

	//Redraw Misa's health bar
	if(misaHealth.getHealth()/100*2.7 !== healthBarF.scaleY){
		healthBarF.setScale(0.5, misaHealth.getHealthPercentage()/100*2.7);
	}
	//Redraw Misa's energy bar
	if(misaEnergy.getEnergy()/100*2.7 !== energyBarF.scaleY){
		energyBarF.setScale(0.9, misaEnergy.getEnergyPercentage()/100*2.7);
		energyBarHL2.setScale(0.9, misaEnergy.getEnergyPercentage()/100*2.7);
	}
  misaEnergy.damageEnergy(0.02);
	//Redraw Misa's XP bar
	if(misaXP.getXP()/100*2.7 !== xpBarF.scaleY){
		xpBarF.setScale(0.5, misaXP.getXPPercentage()/100*2.7);
	}

	// Normalize and scale the velocity so that player can't move faster along a diagonal
	player.body.velocity.normalize().scale(speed);

	// Update the animation last and give left/right animations precedence over up/down animations
	if (cursors.left.isDown) {
		player.anims.play("misa-left-walk", true);
	} else if (cursors.right.isDown) {
		player.anims.play("misa-right-walk", true);
	} else if (cursors.up.isDown) {
		player.anims.play("misa-back-walk", true);
	} else if (cursors.down.isDown) {
		player.anims.play("misa-front-walk", true);
	} else {
		player.anims.stop();

	// If we were moving, pick and idle frame to use
	if (prevVelocity.x < 0) {
		player.setTexture("atlas", "misa-left");	
	} else if (prevVelocity.x > 0) {
		player.setTexture("atlas", "misa-right");
	} else if (prevVelocity.y < 0) {
		player.setTexture("atlas", "misa-back");
	} else if (prevVelocity.y > 0) {
		player.setTexture("atlas", "misa-front");
	}
}
this.setZombieVelocity = function(z){
    const zombieVelocity = z.body.velocity.clone();
    if (zombieVelocity.x < 0) {
		  z.setTexture("atlas_z", "zmisa-left");	
	  } else if (zombieVelocity.x > 0) {
		  z.setTexture("atlas_z", "zmisa-right");
	  } else if (zombieVelocity.y < 0) {
		  z.setTexture("atlas_z", "zmisa-back");
	  }else if (zombieVelocity.y > 0) {
		  z.setTexture("atlas_z", "zmisa-front");
	  }
    return z;
  } 

  this.zombieBehavior = function(z){
    if(z.destination <=0 || (z.body.velocity.x == 0 && z.body.velocity.y == 0)){
      z.body.setVelocity(0);
      z.destination = Math.floor(Math.random() * 256);
      var dir =  Math.floor(Math.random() * 5);
      if(dir == 0){
        z.body.setVelocityX(-speed);
      }else if(dir == 1){
        z.body.setVelocityX(speed);
      }else if(dir == 2){
        z.body.setVelocityY(-speed);
      }else if(dir == 3){
        z.body.setVelocityY(speed);
      }else{
        z.body.setVelocity(0);
        this.setZombieVelocity(z);
      }
    }else{
      z.body.velocity.normalize().scale(speed);
      if (z.body.velocity.x !== 0){
        if(z.body.velocity.x > 0){
          z.anims.play("zmisa-right-walk", true);
        }else{
          z.anims.play("zmisa-left-walk", true);
        }
      }else if(z.body.velocity.y !== 0) {
        if(z.body.velocity.y > 0){
           z.anims.play("zmisa-front-walk", true);
        }else{
          z.anims.play("zmisa-back-walk", true);
        }
	    } else {
		    z.anims.stop();
      }
      z.destination--;
    }
  }

  this.zombieBehavior(zombie0);
  this.zombieBehavior(zombie1);
  this.zombieBehavior(zombie2);
  this.zombieBehavior(zombie3);
  this.zombieBehavior(zombie4);
  this.zombieBehavior(zombie5);
  this.zombieBehavior(zombie6);
  this.zombieBehavior(zombie7);
  this.zombieBehavior(zombie8);
  this.zombieBehavior(zombie9);

}
}
