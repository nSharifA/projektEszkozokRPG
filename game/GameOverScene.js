class GameOverScene extends Phaser.Scene {
	constructor() {
		super({key:"GameOverScene"});
	}

	create() {
		this.add.text(game.scale.width/2-25,game.scale.height/2, 'Game Over', { fontFamily: 'Verdana, "Times New Roman", Tahoma, serif', width: '50px'});
		this.add.text(game.scale.width/2-25,game.scale.height/2 +30, `But such a nice score: ${score}`, { fontFamily: 'Verdana, "Times New Roman", Tahoma, serif', width: '50px'});
		const btnStart = this.add.text(game.scale.width/2 - 25, game.scale.height / 2 + 60, "Back to MainMenu",  { fontFamily: 'Verdana, "Times New Roman", Tahoma, serif', width: '50px'});
		btnStart.setInteractive();

		btnStart.on('pointerdown', () => {
			this.scene.start("WelcomeScene");
		}, this);
	}
}
