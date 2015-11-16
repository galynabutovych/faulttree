import org.apache.commons.collections15.Factory;

public class MyEdgeFactory implements Factory<Integer> {
        private static int linkCount = 0;

        private static MyEdgeFactory instance = new MyEdgeFactory();
        
        private MyEdgeFactory() {            
        }
        
        public static Factory<Integer> getInstance() {
            return instance;
        }
        
        public Integer create() {
            return linkCount++;
        }      
    }