let controls;

class ForestScene extends Phaser.Scene {

	constructor() {
		super({key:"ForestScene"});
	}

	preload() {
		this.load.image("ClassicRPG_Sheet", "../graphics/ClassicRPG_Sheet.png");
		this.load.tilemapTiledJSON("map", "../graphics/forest_map.json");
	}

	create() {
		const map = this.make.tilemap({key: "map"});

		const tileset = map.addTilesetImage("ClassicRPG_Sheet", "ClassicRPG_Sheet");

		const belowLayer = map.createStaticLayer("background", tileset, 0, 0);
		const worldLayer = map.createStaticLayer("obstacle", tileset, 0, 0);

		const camera = this.cameras.main;
		// Set up the arrows to control the camera
		const cursors = this.input.keyboard.createCursorKeys();
		controls = new Phaser.Cameras.Controls.FixedKeyControl({
			camera: camera,
			left: cursors.left,
			right: cursors.right,
			up: cursors.up,
			down: cursors.down,
			speed: 0.5
		});

		// Help text that has a "fixed" position on the screen
		this.add.text(16, 16, "Arrow keys to scroll", {
			font: "18px monospace",
			fill: "#ffffff",
			padding: { x: 20, y: 10 },
			backgroundColor: "#000000"
		}).setScrollFactor(0);
	}
	update(time, delta) {
	  // Apply the controls to the camera each update tick of the game
	  controls.update(delta);
	}
}
