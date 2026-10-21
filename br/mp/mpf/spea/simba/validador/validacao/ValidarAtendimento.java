package br.mp.mpf.spea.simba.validador.validacao;

import br.mp.mpf.asspa.client.exception.NegocioException;
import br.mp.mpf.pgr.client.Context;
import br.mp.mpf.pgr.client.error.ErrorReportCmd;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivo;
import br.mp.mpf.spea.simba.validador.rules.ValidarAgenciaRepetidaRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarContaInvestigadaSemMovimentacaoComExtratoRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarContaInvestigadaSemTitularTRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarContaInvestigadoComMovimentacaoSemExtratoRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarContaRepetidaRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarContaSemMovimentacao;
import br.mp.mpf.spea.simba.validador.rules.ValidarContaSemTitularInvestigado;
import br.mp.mpf.spea.simba.validador.rules.ValidarContaSemTitularesRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarContaSeparadorRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarCpfCnpjTransferenciaTedDocOrigemDestinoRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarDadosObrigatoriosTitularRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarDataAberturaRelacionamentoTitularidadeRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarDataEncerramentoRelacionamentoTitularidadeRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarDepositoDinheiroChequeEleitoralRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarExtratoContaNaoInvestigadaRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarExtratoForaPeriodoAfastamentoRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarExtratoForaPeriodoContaAbertaRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarExtratoSemOrigemDestinoRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarIdentificacaoTedDocRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarIdentificacaoTedDocTransfEleitoralRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarIdentificacaoTransacaoEmEspecieRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarIdentificacaoTransferenciaMesmoBancoRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarInvestigadoCadastroNaoArquivoSimRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarInvestigadoCadastroSimArquivoNaoRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarInvestigadoPessoaInvestigada0Rule;
import br.mp.mpf.spea.simba.validador.rules.ValidarMovimentacaoContaInvestigadoNaoRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarMovimentacaoContaInvestigadoSimRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarTitularUnicoRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarValorExtratoOrigemDestinoRule;
import jakarta.inject.Inject;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import telas.frmSelecionaDiretorio;

@Component
@Scope("prototype")
public class ValidarAtendimento {
   private static final Logger log = LoggerFactory.getLogger(ValidarAtendimento.class);
   @Inject
   private ValidarAgenciaRepetidaRule validarAgenciaRepetidaRule;
   @Inject
   private ValidarContaRepetidaRule validarContaRepetidaRule;
   @Inject
   private ValidarDadosObrigatoriosTitularRule validarDadosObrigatoriosTitularRule;
   @Inject
   private ValidarDataAberturaRelacionamentoTitularidadeRule validarDataAberturaRelacionamentoTitularidadeRule;
   @Inject
   private ValidarDataEncerramentoRelacionamentoTitularidadeRule validarDataEncerramentoRelacionamentoTitularidadeRule;
   @Inject
   private ValidarInvestigadoCadastroSimArquivoNaoRule validarInvestigadoCadastroSimArquivoNaoRule;
   @Inject
   private ValidarInvestigadoPessoaInvestigada0Rule validarInvestigadoPessoaInvestigada0Rule;
   @Inject
   private ValidarInvestigadoCadastroNaoArquivoSimRule validarInvestigadoCadastroNaoArquivoSimRule;
   @Inject
   private ValidarContaSemTitularesRule validarContaSemTitularesRule;
   @Inject
   private ValidarContaInvestigadaSemTitularTRule validarContaInvestigadaSemTitularTRule;
   @Inject
   private ValidarTitularUnicoRule validarTitularUnicoRule;
   @Inject
   private ValidarMovimentacaoContaInvestigadoNaoRule validarMovimentacaoContaInvestigadoNaoRule;
   @Inject
   private ValidarMovimentacaoContaInvestigadoSimRule validarMovimentacaoContaInvestigadoSimRule;
   @Inject
   private ValidarExtratoForaPeriodoAfastamentoRule validarExtratoForaPeriodoAfastamentoRule;
   @Inject
   private ValidarExtratoForaPeriodoContaAbertaRule validarExtratoForaPeriodoContaAbertaRule;
   @Inject
   private ValidarContaInvestigadoComMovimentacaoSemExtratoRule validarContaInvestigadoComMovimentacaoSemExtratoRule;
   @Inject
   private ValidarContaInvestigadaSemMovimentacaoComExtratoRule validarContaInvestigadaSemMovimentacaoComExtratoRule;
   @Inject
   private ValidarExtratoSemOrigemDestinoRule validarExtratoSemOrigemDestinoRule;
   @Inject
   private ValidarValorExtratoOrigemDestinoRule validarValorExtratoOrigemDestinoRule;
   @Inject
   private ValidarIdentificacaoTedDocRule validarIdentificacaoTedDocRule;
   @Inject
   private ValidarIdentificacaoTransferenciaMesmoBancoRule validarIdentificacaoTransferenciaMesmoBancoRule;
   @Inject
   private ValidarIdentificacaoTransacaoEmEspecieRule validarIdentificacaoTransacaoEmEspecieRule;
   @Inject
   private ValidarCpfCnpjTransferenciaTedDocOrigemDestinoRule validarCpfCnpjTransferenciaTedDocOrigemDestinoRule;
   @Inject
   private ValidarIdentificacaoTedDocTransfEleitoralRule validarIdentificacaoTedDocTransfEleitoralRule;
   @Inject
   private ValidarDepositoDinheiroChequeEleitoralRule validarDepositoDinheiroChequeEleitoralRule;
   @Inject
   private ValidarContaSeparadorRule validarContaSeparadorRule;
   @Inject
   private ValidarContaSemTitularInvestigado validarContaSemTitularInvestigado;
   @Inject
   private ValidarContaSemMovimentacao validarContaSemMovimentacao;
   @Inject
   private ValidarExtratoContaNaoInvestigadaRule validarExtratoContaNaoInvestigadaRule;
   @Inject
   private ErrorReportCmd errorReportCmd;
   @Inject
   private Context context;

   public boolean validarArquivoAgencia(frmSelecionaDiretorio.View view) {
      try {
         view.startValidation(NomeArquivo.AGENCIAS);
         view.total(NomeArquivo.AGENCIAS, 1);
         this.validarAgenciaRepetidaRule.validar();
         return !this.errorReportCmd.hasErros();
      } catch (NegocioException var3) {
         return false;
      }
   }

   public boolean validarArquivoConta(frmSelecionaDiretorio.View view) {
      try {
         view.startValidation(NomeArquivo.CONTAS);
         view.total(NomeArquivo.CONTAS, 10);
         this.validarContaSeparadorRule.validar();
         view.increment(NomeArquivo.CONTAS);
         this.validarContaRepetidaRule.validar();
         view.increment(NomeArquivo.CONTAS);
         this.validarContaSemTitularesRule.validar();
         view.increment(NomeArquivo.CONTAS);
         this.validarContaSemTitularInvestigado.validar();
         view.increment(NomeArquivo.CONTAS);
         this.validarContaSemMovimentacao.validar();
         view.increment(NomeArquivo.CONTAS);
         this.validarMovimentacaoContaInvestigadoNaoRule.validar();
         view.increment(NomeArquivo.CONTAS);
         this.validarTitularUnicoRule.validar();
         view.increment(NomeArquivo.CONTAS);
         this.validarMovimentacaoContaInvestigadoSimRule.validar();
         view.increment(NomeArquivo.CONTAS);
         this.validarContaInvestigadoComMovimentacaoSemExtratoRule.validar();
         view.increment(NomeArquivo.CONTAS);
         this.validarContaInvestigadaSemMovimentacaoComExtratoRule.validar();
         return !this.errorReportCmd.hasErros();
      } catch (NegocioException var3) {
         return false;
      }
   }

   public boolean validarArquivoTitularidade(frmSelecionaDiretorio.View view) {
      try {
         view.startValidation(NomeArquivo.TITULARES);
         view.total(NomeArquivo.TITULARES, 7);
         this.validarDadosObrigatoriosTitularRule.validar();
         view.increment(NomeArquivo.TITULARES);
         this.validarDataAberturaRelacionamentoTitularidadeRule.validar();
         view.increment(NomeArquivo.TITULARES);
         this.validarDataEncerramentoRelacionamentoTitularidadeRule.validar();
         view.increment(NomeArquivo.TITULARES);
         this.validarInvestigadoCadastroSimArquivoNaoRule.validar();
         view.increment(NomeArquivo.TITULARES);
         this.validarInvestigadoPessoaInvestigada0Rule.validar();
         view.increment(NomeArquivo.TITULARES);
         this.validarInvestigadoCadastroNaoArquivoSimRule.validar();
         view.increment(NomeArquivo.TITULARES);
         this.validarContaInvestigadaSemTitularTRule.validar();
         return !this.errorReportCmd.hasErros();
      } catch (NegocioException var3) {
         return false;
      }
   }

   private void time(String msg, Consumer<Void> fn) {
      StopWatch stopWatch = new StopWatch();
      stopWatch.start();
      fn.accept((Object)null);
      stopWatch.stop();
      log.debug(msg + ". Tempo: {} ms", stopWatch.getTotalTimeMillis());
   }

   public boolean validarArquivoExtrato(frmSelecionaDiretorio.View view) {
      try {
         int totalValidacoes = this.context.isEleitoral() ? 6 : 5;
         view.total(NomeArquivo.EXTRATO, totalValidacoes);
         view.startValidation(NomeArquivo.EXTRATO);
         StopWatch stopWatch = new StopWatch();
         stopWatch.start();

         try {
            this.time("Validação EXTRATO 1", (var1) -> this.validarExtratoForaPeriodoAfastamentoRule.validar());
            view.increment(NomeArquivo.EXTRATO);
            this.time("Validação EXTRATO 2", (var1) -> this.validarExtratoForaPeriodoContaAbertaRule.validar());
            view.increment(NomeArquivo.EXTRATO);
            this.time("Validação EXTRATO 3", (var1) -> this.validarExtratoSemOrigemDestinoRule.validar());
            view.increment(NomeArquivo.EXTRATO);
            this.time("Validação EXTRATO 4", (var1) -> this.validarExtratoContaNaoInvestigadaRule.validar());
            view.increment(NomeArquivo.EXTRATO);
            this.time("Validação EXTRATO 5", (var1) -> this.validarValorExtratoOrigemDestinoRule.validar());
         } finally {
            stopWatch.stop();
            log.debug("Tempo validação conteúdo arquivo ORIGEM_DESTINO: {} ms", stopWatch.getTotalTimeMillis());
         }

         return !this.errorReportCmd.hasErros();
      } catch (NegocioException var8) {
         return false;
      }
   }

   public boolean validarArquivoOrigemDestino(frmSelecionaDiretorio.View view) {
      try {
         int totalValidacoes = this.context.isEleitoral() ? 3 : 4;
         view.total(NomeArquivo.ORIGEM_DESTINO, totalValidacoes);
         view.startValidation(NomeArquivo.ORIGEM_DESTINO);
         StopWatch stopWatch = new StopWatch();
         stopWatch.start();

         try {
            if (this.context.isEleitoral()) {
               this.time("Validação ORIGEM_DESTINO 1", (var1) -> this.validarIdentificacaoTedDocTransfEleitoralRule.validar());
               view.increment(NomeArquivo.ORIGEM_DESTINO);
               this.time("Validação ORIGEM_DESTINO 2", (var1) -> this.validarDepositoDinheiroChequeEleitoralRule.validar());
               view.increment(NomeArquivo.ORIGEM_DESTINO);
            } else {
               this.time("Validação ORIGEM_DESTINO 3", (var1) -> this.validarIdentificacaoTedDocRule.validar());
               view.increment(NomeArquivo.ORIGEM_DESTINO);
               this.time("Validação ORIGEM_DESTINO 4", (var1) -> this.validarIdentificacaoTransferenciaMesmoBancoRule.validar());
               view.increment(NomeArquivo.ORIGEM_DESTINO);
               this.time("Validação ORIGEM_DESTINO 5", (var1) -> this.validarIdentificacaoTransacaoEmEspecieRule.validar());
               view.increment(NomeArquivo.ORIGEM_DESTINO);
            }

            this.time("Validação ORIGEM_DESTINO 6", (var1) -> this.validarCpfCnpjTransferenciaTedDocOrigemDestinoRule.validar());
         } finally {
            stopWatch.stop();
            log.debug("Tempo validação conteúdo arquivo ORIGEM_DESTINO: {} ms", stopWatch.getTotalTimeMillis());
         }

         return !this.errorReportCmd.hasErros();
      } catch (NegocioException var8) {
         return false;
      }
   }
}
