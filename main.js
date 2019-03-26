var config = {
	type: Phaser.AUTO,
	width: 800,
	height: 600,
	antialias: false,
	transparent: false,
	physics: {
		default: 'arcade',
		gravity: { y: 0 }
	},
	scene: [ WelcomeScene, ForestScene ]
};

var game = new Phaser.Game(config);