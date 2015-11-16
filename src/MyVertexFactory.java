import org.apache.commons.collections15.Factory;

public class MyVertexFactory implements Factory<MyVertex> {
        private static int nodeCount = 0;
        private static MyVertexFactory instance = new MyVertexFactory();
        
        private MyVertexFactory() {            
        }
        
        public static MyVertexFactory getInstance() {
            return instance;
        }
        
        public MyVertex create() {
            String name = "Location " + nodeCount++;
            MyVertex v = new MyVertex(name);
            return v;
        }        
    }