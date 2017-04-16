package com.bclaus.rsps.server.vd.objects.cannon;

import com.bclaus.rsps.server.vd.world.Position;

/**
 * @author lare96
 */
public enum FireDirection {

	NORTH(516) {
		@Override
		public boolean canTarget(Position cannonPosition, Position targetPosition) {
			return (targetPosition.getY() > cannonPosition.getY() && targetPosition.getX() >= cannonPosition.getX() - 1 && targetPosition.getX() <= cannonPosition.getX() + 1);
		}
	},
	NORTH_EAST(517) {
		@Override
		public boolean canTarget(Position cannonPosition, Position targetPosition) {
			return (targetPosition.getX() >= cannonPosition.getX() + 1 && targetPosition.getY() >= cannonPosition.getY() + 1);
		}
	},
	EAST(518) {
		@Override
		public boolean canTarget(Position cannonPosition, Position targetPosition) {
			return (targetPosition.getX() > cannonPosition.getX() && targetPosition.getY() >= cannonPosition.getY() - 1 && targetPosition.getY() <= cannonPosition.getY() + 1);
		}
	},
	SOUTH_EAST(519) {
		@Override
		public boolean canTarget(Position cannonPosition, Position targetPosition) {
			return (targetPosition.getY() <= cannonPosition.getY() - 1 && targetPosition.getX() >= cannonPosition.getX() + 1);
		}
	},
	SOUTH(520) {
		@Override
		public boolean canTarget(Position cannonPosition, Position targetPosition) {
			return (targetPosition.getY() < cannonPosition.getY() && targetPosition.getX() >= cannonPosition.getX() - 1 && targetPosition.getX() <= cannonPosition.getX() + 1);
		}
	},
	SOUTH_WEST(521) {
		@Override
		public boolean canTarget(Position cannonPosition, Position targetPosition) {
			return (targetPosition.getX() <= cannonPosition.getX() - 1 && targetPosition.getY() <= cannonPosition.getY() - 1);
		}
	},
	WEST(514) {
		@Override
		public boolean canTarget(Position cannonPosition, Position targetPosition) {
			return (targetPosition.getX() < cannonPosition.getX() && targetPosition.getY() >= cannonPosition.getY() - 1 && targetPosition.getY() <= cannonPosition.getY() + 1);
		}
	},
	NORTH_WEST(515) {
		@Override
		public boolean canTarget(Position cannonPosition, Position targetPosition) {
			return (targetPosition.getX() <= cannonPosition.getX() - 1 && targetPosition.getY() >= cannonPosition.getY() + 1);
		}
	};

	private int objectAnimation;

	private FireDirection(int objectAnimation) {
		this.objectAnimation = objectAnimation;
	}

	public abstract boolean canTarget(Position cannonPosition, Position targetPosition);

	public int getObjectAnimation() {
		return objectAnimation;
	}
}