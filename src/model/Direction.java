package model;

public enum Direction {

	PLACE_TO_TRANSITION {
		@Override
		public boolean canFire(Place p, int weight) {
			return p.hasAtLeastTokens(weight);
		}

		@Override
		public void fire(Place p, int weight) {
			p.removeTokens(weight);
		}
	},

	TRANSITION_TO_PLACE {
		@Override
		public boolean canFire(Place p, int weight) {
			return !p.maxTokensReached(weight);
		}

		@Override
		public void fire(Place p, int weight) {
			p.addTokens(weight);
		}
	};

	public abstract boolean canFire(Place p, int weight);

	public abstract void fire(Place p, int weight);
}
