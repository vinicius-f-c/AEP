package br.edu.ads.reciclagem;

import br.edu.ads.reciclagem.controlador.EcoTechController;
import br.edu.ads.reciclagem.infra.ConexaoBancoDados;
import br.edu.ads.reciclagem.repositorio.DoadorDAO;
import br.edu.ads.reciclagem.repositorio.ItemEletronicoDAO;
import br.edu.ads.reciclagem.servico.DoadorService;
import br.edu.ads.reciclagem.servico.ItemEletronicoService;
import br.edu.ads.reciclagem.visao.ConsoleView;

public class Main {

    public static void main(String[] args) {
        ConexaoBancoDados conexao = new ConexaoBancoDados();
        ConsoleView view = new ConsoleView();

        if (!conexao.testarConexao()) {
            view.erro("Não foi possível conectar ao MySQL. Verifique se o servidor está ligado, "
                    + "se o script database/script_banco.sql foi executado e as variáveis "
                    + "DB_URL, DB_USER e DB_PASS.");
            return;
        }
        view.mensagem("Conexão com o banco de dados estabelecida.");

        DoadorDAO doadorDAO = new DoadorDAO(conexao);
        ItemEletronicoDAO itemDAO = new ItemEletronicoDAO(conexao);
        DoadorService doadorService = new DoadorService(doadorDAO);
        ItemEletronicoService itemService = new ItemEletronicoService(itemDAO, doadorDAO);

        new EcoTechController(view, doadorService, itemService).executar();
    }
}
