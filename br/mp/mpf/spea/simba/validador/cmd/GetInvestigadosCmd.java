package br.mp.mpf.spea.simba.validador.cmd;

import DAO.BancoHSQLDB;
import DAO.Mapa;
import br.mp.mpf.spea.simba.validador.domain.Investigado;
import br.mp.mpf.spea.simba.validador.enums.CamposInvestigado;
import br.mp.mpf.spea.simba.validador.enums.SimNao;
import br.mp.mpf.spea.simba.validador.enums.TipoPessoaOrigemDestino;
import jakarta.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GetInvestigadosCmd {
   private Logger log = LoggerFactory.getLogger(GetInvestigadosCmd.class);
   @Inject
   private BancoHSQLDB banco;

   public List<Investigado> get() {
      String[] var10000 = Mapa.getNomesTabelasB();
      String sql = "select * from " + var10000[7];

      try {
         ResultSet rs = this.banco.executaQuery(sql);
         List<Investigado> investigados = new ArrayList();

         while(rs.next()) {
            investigados.add(this.from(rs));
         }

         return investigados;
      } catch (SQLException e) {
         this.log.error("Erro ao recuperar investigados", e);
         return null;
      }
   }

   private Investigado from(ResultSet rs) throws SQLException {
      Investigado investigado = new Investigado();
      String[] campos = Mapa.getCamposTabelasB()[7];
      investigado.setCodigo(rs.getLong(campos[CamposInvestigado.CODIGO_INVESTIGADO.ordinal()]));
      String tipoPessoa = rs.getString(campos[CamposInvestigado.TIPO_PESSOA.ordinal()]);
      investigado.setTipoPessoa("1".equals(tipoPessoa) ? TipoPessoaOrigemDestino.PF : TipoPessoaOrigemDestino.PJ);
      investigado.setCpfCnpj(rs.getString(campos[CamposInvestigado.CPF_CNPJ_PESSOA.ordinal()]));
      investigado.setNomePessoa(rs.getString(campos[CamposInvestigado.NOME_PESSOA.ordinal()]));
      investigado.setTeveRelacionamento(rs.getInt(campos[CamposInvestigado.TEVE_RELACIONAMENTO.ordinal()]) != 0 ? SimNao.SIM : SimNao.NAO);
      investigado.setTeveConta(rs.getInt(campos[CamposInvestigado.TEVE_CONTA.ordinal()]) != 0 ? SimNao.SIM : SimNao.NAO);
      investigado.setTeveBdv(rs.getInt(campos[CamposInvestigado.TEVE_BDV.ordinal()]) != 0 ? SimNao.SIM : SimNao.NAO);
      investigado.setObservacao(rs.getString(campos[CamposInvestigado.OBSERVACAO.ordinal()]));
      investigado.setDataInicioAfastamento(rs.getDate(campos[CamposInvestigado.DATA_INICIO_AFASTAMENTO.ordinal()]).toLocalDate());
      investigado.setDataFimAfastamento(rs.getDate(campos[CamposInvestigado.DATA_FIM_AFASTAMENTO.ordinal()]).toLocalDate());
      return investigado;
   }
}
