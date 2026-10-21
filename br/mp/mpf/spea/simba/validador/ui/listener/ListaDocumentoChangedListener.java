package br.mp.mpf.spea.simba.validador.ui.listener;

import br.mp.mpf.spea.simba.validador.to.Documento;
import java.util.List;

public interface ListaDocumentoChangedListener {
   void onListaDocumentoChanged(List<Documento> documentos);
}
