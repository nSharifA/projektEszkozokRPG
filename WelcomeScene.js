class WelcomeScene extends Phaser.Scene {
	constructor() {
		super({key:"WelcomeScene"});
	}

	preload() {
		this.load.image('SCENE_WELCOME_BG', 'graphics/welcome_background.jpg');
	}

	create() {
		this.image = this.add.image(400,300,'SCENE_WELCOME_BG');
		this.add.text(game.scale.width/2-25,game.scale.height/2, 'ElteRPG', { fontFamily: 'Verdana, "Times New Roman", Tahoma, serif', width: '50px'});
		const btnStart = this.add.text(game.scale.width/2 - 25, game.scale.height / 2 + 30, "Start",  { fontFamily: 'Verdana, "Times New Roman", Tahoma, serif', width: '50px'});
		btnStart.setInteractive();

		btnStart.on('pointerdown', () => {
			this.scene.start("ForestScene");
		}, this);
	}
}
