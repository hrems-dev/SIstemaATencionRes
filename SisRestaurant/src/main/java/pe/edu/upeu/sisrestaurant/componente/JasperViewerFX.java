package pe.edu.upeu.sisrestaurant.componente;

import javafx.embed.swing.SwingNode;
import javafx.scene.layout.BorderPane;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.jasperreports.swing.JRViewer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JasperViewerFX extends BorderPane {
    private JRViewer viewer;
}
