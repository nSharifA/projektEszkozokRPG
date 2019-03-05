class WelcomeScene extends Phaser.Scene {
	constructor() {
		super({key:"WelcomeScene"});
	}

	preload() {
		this.load.image('SCENE_WELCOME_BG', 'graphics/welcome_background.jpg');
	}

	create() {
		this.image = this.add.image(400,300,'SCENE_WELCOME_BG');
	}
}
