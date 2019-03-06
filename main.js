var config = {
	type: Phaser.AUTO,
	width: 800,
	height: 600,
	physics: {
		default: 'arcade',
		gravity: { y: 0 }
	},
	scene: [ WelcomeScene, ForestScene ]
};

var game = new Phaser.Game(config);