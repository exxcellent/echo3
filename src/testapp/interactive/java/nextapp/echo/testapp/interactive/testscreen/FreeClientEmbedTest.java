package nextapp.echo.testapp.interactive.testscreen;
import nextapp.echo.app.Button;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.ContentPane;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.SplitPane;
import nextapp.echo.app.WindowPane;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.layout.SplitPaneLayoutData;
import nextapp.echo.app.util.Context;
import nextapp.echo.testapp.interactive.ButtonColumn;
import nextapp.echo.testapp.interactive.StyleUtil;
import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

public class FreeClientEmbedTest extends SplitPane {

    private static final Service EMBED_TEST_COMPONENT_SERVICE = JavaScriptService.forResource("Test.EmbedTestComponent", 
            "/nextapp/echo/testapp/interactive/resource/js/EmbedTestComponent.js");
    
    static {
        WebContainerServlet.getServiceRegistry().add(EMBED_TEST_COMPONENT_SERVICE);
    }
    
    public class EmbedTestComponent extends Component {
        
        public static final String PROPERTY_TEXT = "text";
        
        public String getText() {
            return (String) getProperty(PROPERTY_TEXT);
        }
        
        public void setText(String newValue) {
            setProperty(PROPERTY_TEXT, newValue);
        }
    }
    
    public static class EmbedTestComponentPeer extends AbstractComponentSynchronizePeer {
    
        public EmbedTestComponentPeer() {
            super();
            addRequiredComponentClass(Label.class);
        }
    
        /**
         * @see nextapp.echo.webcontainer.AbstractComponentSynchronizePeer#getClientComponentType()
         */
        public String getClientComponentType() {
            return "EmbedTestComponent";
        }

        /**
         * @see nextapp.echo.webcontainer.AbstractComponentSynchronizePeer#getComponentClass()
         */
        public Class getComponentClass() {
            return EmbedTestComponent.class;
        }
        
        /**
         * @see nextapp.echo.webcontainer.ComponentSynchronizePeer#init(nextapp.echo.app.util.Context)
         */
        public void init(Context context) {
            super.init(context);
            ServerMessage serverMessage = (ServerMessage) context.get(ServerMessage.class);
            serverMessage.addLibrary(EMBED_TEST_COMPONENT_SERVICE.getId());
        }
    }

    private class EmbedTestPane extends Component {
        
    }
    
    public static class EmbedTestPanePeer extends AbstractComponentSynchronizePeer {
    
        public EmbedTestPanePeer() {
            super();
            addRequiredComponentClass(Button.class);
            addRequiredComponentClass(Column.class);
            addRequiredComponentClass(ContentPane.class);
            addRequiredComponentClass(Label.class);
            addRequiredComponentClass(Row.class);
            addRequiredComponentClass(WindowPane.class);
        }
    
        /**
         * @see nextapp.echo.webcontainer.AbstractComponentSynchronizePeer#getClientComponentType()
         */
        public String getClientComponentType() {
            return "EmbedTestPane";
        }

        /**
         * @see nextapp.echo.webcontainer.AbstractComponentSynchronizePeer#getComponentClass()
         */
        public Class getComponentClass() {
            return EmbedTestPane.class;
        }

        /**
         * @see nextapp.echo.webcontainer.ComponentSynchronizePeer#init(nextapp.echo.app.util.Context)
         */
        public void init(Context context) {
            super.init(context);
            ServerMessage serverMessage = (ServerMessage) context.get(ServerMessage.class);
            serverMessage.addLibrary(EMBED_TEST_COMPONENT_SERVICE.getId());
        }
    }

    private Column testColumn;
    private EmbedTestComponent embedTestComponent;
    
    public FreeClientEmbedTest() {
        super(SplitPane.ORIENTATION_HORIZONTAL, new Extent(250, Extent.PX));
        setStyleName("DefaultResizable");

        SplitPaneLayoutData splitPaneLayoutData;

        ButtonColumn controlsColumn = new ButtonColumn();
        controlsColumn.setStyleName("TestControlsColumn");
        add(controlsColumn);
        
        controlsColumn.addButton("Change Background Color of Container", new ActionListener() {
        
            public void actionPerformed(ActionEvent e) {
                testColumn.setBackground(StyleUtil.randomColor());
            }
        });

        controlsColumn.addButton("Set component text: null", new ActionListener() {
        
            public void actionPerformed(ActionEvent e) {
                embedTestComponent.setText(null);
            }
        });

        controlsColumn.addButton("Set component text: \"text\"", new ActionListener() {
        
            public void actionPerformed(ActionEvent e) {
                embedTestComponent.setText("text");
            }
        });

        testColumn = new Column();
        testColumn.setCellSpacing(new Extent(15));
        splitPaneLayoutData = new SplitPaneLayoutData();
        splitPaneLayoutData.setInsets(new Insets(15));
        testColumn.setLayoutData(splitPaneLayoutData);
        add(testColumn);
        
        embedTestComponent = new EmbedTestComponent();
        testColumn.add(embedTestComponent);
        
        testColumn.add(new EmbedTestPane());
    }
}
