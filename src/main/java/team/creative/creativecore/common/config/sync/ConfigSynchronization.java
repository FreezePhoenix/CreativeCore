package team.creative.creativecore.common.config.sync;

import net.fabricmc.api.EnvType;

public enum ConfigSynchronization {
    
    CLIENT {
        @Override
        public boolean useFolder(boolean forceSynchronization, EnvType side) {
            if (forceSynchronization) {
                return side == EnvType.SERVER;
            }
            return side == EnvType.CLIENT;
        }
        
        @Override
        public boolean useValue(boolean forceSynchronization, EnvType side) {
            if (forceSynchronization)
                return side == EnvType.SERVER;
            return side == EnvType.SERVER;
        }
    },
    UNIVERSAL {
        @Override
        public boolean useFolder(boolean forceSynchronization, EnvType side) {
            return true;
        }
        
        @Override
        public boolean useValue(boolean forceSynchronization, EnvType side) {
            return side == EnvType.SERVER;
        }
    },
    SERVER {
        @Override
        public boolean useFolder(boolean forceSynchronization, EnvType side) {
            return side == EnvType.SERVER;
        }
        
        @Override
        public boolean useValue(boolean forceSynchronization, EnvType side) {
            return side == EnvType.SERVER;
        }
    };
    
    public abstract boolean useFolder(boolean forceSynchronization, EnvType side);
    
    public abstract boolean useValue(boolean forceSynchronization, EnvType side);
    
}
