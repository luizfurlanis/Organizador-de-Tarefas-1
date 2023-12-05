
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entidades.Tarefa;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.CheckBox;

public class App extends Application{
    //private static Stage stage = null;
    private ArrayList<Tarefa> lista = new ArrayList<>();
    private boolean prioridade = false; 

    ContextMenu contextMenuTarefas = new ContextMenu();
    MenuItem deletarItem = new MenuItem("Excluir");
    private ListView<Tarefa> listasPadrão = new ListView<>();


    

    public static void main(String[] args) throws Exception {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }

    @Override
    public void start(Stage stage) throws Exception{  
        
        //logica de ler o arquivo json sempre que abrir o app//

        // Verifica se o arquivo JSON existe
        java.nio.file.Path path = Paths.get("C:\\Users\\furla\\Documents\\GitHub\\Organizador-de-Tarefas\\OrganizadorDeTarefas\\src\\testes\\dados.json");
        if (Files.exists(path)) {
            // Se o arquivo existe, lê as tarefas do JSON
            lerTarefasDoJson();
        }


        //Tela inicial
        stage.initStyle(StageStyle.UNDECORATED);
        
        stage.setTitle("Tela inicial");
        //Elementos da tela inicial 
        Button botaoCriarTarefa = new Button("Nova Tarefa");
        Button botaoVerTarefas = new Button("Ver todas Tarefas");
        Button botaoFecharapp = new Button("x");    
        Button botaoMinimizaApp = new Button("-");
        
        //add os estilos nos botoes da tela inicial 
        botaoCriarTarefa.getStyleClass().add("rounded-buttonPRINCIPAL");
        botaoVerTarefas.getStyleClass().add("rounded-buttonPRINCIPAL");
        botaoMinimizaApp.setStyle("-fx-background-color: #422402; -fx-text-fill: white; -fx-font-size: 12px;");
        botaoMinimizaApp.setOnMouseEntered(e -> botaoMinimizaApp.setStyle("-fx-background-color: #c44800; -fx-text-fill: white; -fx-font-size: 12px;"));
        botaoMinimizaApp.setOnMouseExited(e -> botaoMinimizaApp.setStyle("-fx-background-color: #422402; -fx-text-fill: white; -fx-font-size: 12px;"));
        botaoFecharapp.setStyle("-fx-background-color: #422402; -fx-text-fill: white; -fx-font-size: 12px;");
        botaoFecharapp.setOnMouseEntered(e -> botaoFecharapp.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white; -fx-font-size: 12px;"));
        botaoFecharapp.setOnMouseExited(e -> botaoFecharapp.setStyle("-fx-background-color: #422402; -fx-text-fill: white; -fx-font-size: 12px;"));
        // add imagem ao plano de fundo do app
        Image FundoDaTelaInicialIMAGE = new Image(getClass().getResourceAsStream("/imagens/TelaUM.jpg"));
        BackgroundImage FundoDaTelaInicial = new BackgroundImage(FundoDaTelaInicialIMAGE, 
        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, 
        BackgroundSize.DEFAULT);
        //criando layout da tela inicial
        AnchorPane TelaInicial = new AnchorPane();
        TelaInicial.getChildren().addAll(botaoCriarTarefa, botaoVerTarefas, botaoFecharapp, botaoMinimizaApp); // add os elemetos na tela
        TelaInicial.setBackground(new Background(FundoDaTelaInicial));
        //POSIÇÂO DOS ELEMENTOS NA TELA INICIAL
        AnchorPane.setTopAnchor(botaoCriarTarefa, 50.00);
        AnchorPane.setLeftAnchor(botaoCriarTarefa, 85.00);
        AnchorPane.setTopAnchor(botaoVerTarefas, 150.00);
        AnchorPane.setLeftAnchor(botaoVerTarefas, 63.00);
        AnchorPane.setTopAnchor(botaoFecharapp, 5.00);
        AnchorPane.setLeftAnchor(botaoFecharapp, 265.00);
        AnchorPane.setTopAnchor(botaoMinimizaApp, 5.00);
        AnchorPane.setLeftAnchor(botaoMinimizaApp, 240.00);
        
        // cria a cena e configura o tamanho 
        Scene tela = new Scene(TelaInicial, 300,250);
        //vincula no arquivo CSS
        tela.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // proibe o usuario mudar o tamanho da janela 
        stage.resizableProperty().setValue(Boolean.FALSE);;
        //atribui uma tela pra cena
        stage.setScene(tela);
        stage.show();

        //Criação da tela de Criação de tarefa
        Stage telaCriar = new Stage();
        //tira a barra da janela 
        telaCriar.initStyle(StageStyle.TRANSPARENT);
        //importa a imagem de fundo
        Image FundoDaTelaCriarIMAGE = new Image(getClass().getResourceAsStream("/imagens/TelaCRIAR.jpg"));
        //define um objeto para a imagem de fundo
        BackgroundImage FundoDaTelaCriar = new BackgroundImage(FundoDaTelaCriarIMAGE, 
        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, 
        BackgroundSize.DEFAULT);
                
        Button btnSalvar = new Button("Salvar");
        Button btnVoltarTelaInicial = new Button("Voltar");
        Button btnPrioridadeSIM = new Button("Ativar");
        Label lblNome = new Label("Nome:");
        Label lblDescricao = new Label("Descrição:");
        Label lblPrioridade = new Label("Prioridade:");
        TextField txtNome = new TextField();
        TextArea txtDescricao = new TextArea();
        //configurando os elementos
        txtDescricao.setWrapText(true);
        txtDescricao.setStyle("-fx-text-fill: black; -fx-background-color: #c09f83;");
        txtNome.setStyle("-fx-text-fill: black; -fx-background-color: #c09f83;");
        
        txtDescricao.setMaxSize(400,160);
        // add estilo ao elementos da tela de criação
        btnSalvar.getStyleClass().add("rounded-button");
        btnVoltarTelaInicial.getStyleClass().add("rounded-button");
        btnPrioridadeSIM.getStyleClass().add("rounded-button");
        //muda cor das label
        //lblNome.setStyle("-fx-text-fill: #301b03");
        lblNome.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #301b03;");
        lblDescricao.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #301b03;");
        lblPrioridade.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #301b03;");

        AnchorPane TelaCriacaoDeTarefa = new AnchorPane();
        // Define o fundo da tela 
        TelaCriacaoDeTarefa.setBackground(new Background(FundoDaTelaCriar));
        //TelaCriacaoDeTarefa.setStyle("-fx-background-color: #c09f83;");
        //add elementos na tela
        TelaCriacaoDeTarefa.getChildren().addAll(btnSalvar,btnVoltarTelaInicial , btnPrioridadeSIM, lblNome,
        lblDescricao, lblPrioridade, txtNome, txtDescricao);

        AnchorPane.setTopAnchor(btnSalvar, 230.00);
        AnchorPane.setLeftAnchor(btnSalvar, 450.00);

        AnchorPane.setTopAnchor(btnVoltarTelaInicial, 270.00);
        AnchorPane.setLeftAnchor(btnVoltarTelaInicial, 20.00);

        AnchorPane.setTopAnchor(btnPrioridadeSIM, 220.00);
        AnchorPane.setLeftAnchor(btnPrioridadeSIM, 105.00);

        AnchorPane.setTopAnchor(lblNome, 14.00);
        AnchorPane.setLeftAnchor(lblNome, 10.00);

        AnchorPane.setTopAnchor(lblDescricao, 45.00);
        AnchorPane.setLeftAnchor(lblDescricao, 10.00);

        AnchorPane.setTopAnchor(lblPrioridade, 227.00);
        AnchorPane.setLeftAnchor(lblPrioridade, 10.00);

        AnchorPane.setTopAnchor(txtNome, 10.00);
        AnchorPane.setLeftAnchor(txtNome, 55.00);

        AnchorPane.setTopAnchor(txtDescricao,50.00);
        AnchorPane.setLeftAnchor(txtDescricao, 85.00);

        //cria uma cena pra tela 
        telaCriar.setScene(new Scene(TelaCriacaoDeTarefa, 600, 320));

        
        //add estilo na cena
        TelaCriacaoDeTarefa.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        

        //Criação da tela de ver todas Tarefas 

        Stage TelaListagemDeTarefas = new Stage();
        TelaListagemDeTarefas.initStyle(StageStyle.UNDECORATED);

        listasPadrão.getItems().addAll(lista);
        Button btVoltar = new Button("Voltar");
        //add estilo aos elementos
        btVoltar.getStyleClass().add("rounded-button");
        listasPadrão.getStylesheets().add(getClass().getResource("listastyle.css").toExternalForm());
        listasPadrão.setPrefSize(350, 340);

        VBox vBoxListas = new VBox();
        vBoxListas.getChildren().add(listasPadrão);
        
        vBoxListas.setPrefSize(375, 340);

        AnchorPane pnlTelaVerListas = new AnchorPane();
        pnlTelaVerListas.getChildren().addAll(vBoxListas, btVoltar);

        AnchorPane.setTopAnchor(btVoltar, 350.00);
        AnchorPane.setLeftAnchor(btVoltar, 20.00);

        Scene cenaTelaverTarefas = new Scene(pnlTelaVerListas, 400, 400);
        TelaListagemDeTarefas.setScene(cenaTelaverTarefas);

        Image FundoDaTelaListagemdeTarefasIMAGE = new Image(getClass().getResourceAsStream("/imagens/telaListas.jpg"));
        BackgroundImage FundoDaTelaListagemdeTarefas = new BackgroundImage(FundoDaTelaListagemdeTarefasIMAGE, 
        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, 
        BackgroundSize.DEFAULT);
        pnlTelaVerListas.setBackground(new Background(FundoDaTelaListagemdeTarefas));
        vBoxListas.setBackground(new Background(FundoDaTelaListagemdeTarefas));
        listasPadrão.setBackground(new Background(FundoDaTelaListagemdeTarefas));
        pnlTelaVerListas.getStylesheets().add(getClass().getResource("style.css").toExternalForm());



        //Metodos dos Botões

        //ação botao fechar app
        botaoFecharapp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                stage.close();
            }
        });

        //ação botao minimizar
        botaoMinimizaApp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                stage.setIconified(true);
            }
        });



        //ação do botao Criar Tarefa
        botaoCriarTarefa.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                
                //Fecha a tela inicial
                stage.close();
                telaCriar.show();
        }
        });
        btVoltar.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event){
                    TelaListagemDeTarefas.close();
                    stage.show();
                }
            });

        btnVoltarTelaInicial.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event){
                    telaCriar.close();
                    stage.show();
                }
            });


            btnPrioridadeSIM.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event){
                    prioridade = true;
                    System.out.println(prioridade);
                }
            });

            btnSalvar.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Tarefa t = new Tarefa(txtNome.getText(), txtDescricao.getText(), prioridade, false);

                lista.add(t); // Adiciona à lista
                salvarTarefasNoJson(); // Salva a lista no JSON

                // Adiciona à ListView
                listasPadrão.getItems().add(t);
                txtNome.clear();
                txtDescricao.clear();
                System.out.println(t.getNome());
                System.out.println(t.getDescricao());
                System.out.println(t.isPrioridade());
            }
            });

         //ação botao ver listas 

        

        botaoVerTarefas.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                stage.close();
                TelaListagemDeTarefas.show();
            }
        });


        //metodo remover tarefas

        contextMenuTarefas.getItems().addAll(deletarItem);
    
            // Configurando o evento de menu de contexto para a ListView de Tarefas
            listasPadrão.setOnContextMenuRequested((ContextMenuEvent event) -> {
                contextMenuTarefas.show(listasPadrão, event.getScreenX(), event.getScreenY());
            });
            // Adicionando ações ao item do menu
            deletarItem.setOnAction(event -> {
                Tarefa selectedItem = listasPadrão.getSelectionModel().getSelectedItem(); // verifica a linha do item selecionado na listview para poder realizar ações com ele no saco pega o item selecionado e deleta
                System.out.println("Apagar: " + selectedItem.getNome());
    
                // Remover a tarefa da lista
                if (selectedItem != null) {
                    lista.remove(selectedItem);//remove da lista de suporte
                    salvarTarefasNoJson(); // Salvar a lista atualizada no JSON
                    listasPadrão.getItems().remove(selectedItem);//apaga da list view
                }
            });

            //configuração para aparecer somente o nome da tarefa na listview
            listasPadrão.setCellFactory(param -> new ListCell<Tarefa>() {
            @Override
            protected void updateItem(Tarefa tarefa, boolean vazio) {
                super.updateItem(tarefa, vazio);
                if (vazio || tarefa == null) {
                    setText(null);
                } else {
                    setText(tarefa.getNome());
                }
            }
        });

        //configuração do check box
        listasPadrão.setCellFactory(param -> new ListCell<Tarefa>() {
            private final CheckBox checkBox = new CheckBox();
        
            @Override
            protected void updateItem(Tarefa item, boolean vazio) {
                super.updateItem(item, vazio);
                if (vazio || item == null) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item.isStatus());
                    checkBox.setText(item.getNome());
        
                    // Atualizar o atributo status ao clicar na CheckBox
                    checkBox.setOnAction(event -> {
                        item.setStatus(checkBox.isSelected());
                        salvarTarefasNoJson(); // Salvar a lista atualizada no JSON
                    });
        
                    setGraphic(checkBox);
        
                    // Adicionar Tooltip para indicar se a tarefa está concluída ou não
                    Tooltip tooltip = new Tooltip(item.getDescricao());
                    setTooltip(tooltip);
                }
            }
        });
        
        
    }
    private void lerTarefasDoJson() {
        try {
        Gson gson = new Gson();

        // Lê o arquivo JSON e converte pra lista de Tarefas
        List<Tarefa> tarefas = gson.fromJson(
                new FileReader("C:\\Users\\furla\\Documents\\GitHub\\Organizador-de-Tarefas\\OrganizadorDeTarefas\\src\\testes\\dados.json"),
                new TypeToken<List<Tarefa>>() {}.getType());

          // Limpa a ListView antes de adicionar os itens
        listasPadrão.getItems().clear();

          // Adiciona as tarefas lidas na lista, sem duplicar
        for (Tarefa tarefa : tarefas) {
            if (!lista.contains(tarefa)) {
                lista.add(tarefa);
            }
        }

        //cria uma coleção observável, necessaria para adicionar depois na listview(serve de ponte pq o metodo nao exixte para objetos do tipo listview)
        ObservableList<Tarefa> tarefasObservable = FXCollections.observableArrayList(tarefas);

        // Adiciona as tarefas lidas à ListView
        listasPadrão.getItems().addAll(tarefasObservable);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    }

    //metodo para salvar as tarefas no json
    private void salvarTarefasNoJson() {
        try (FileWriter writer = new FileWriter(
                    "C:\\Users\\furla\\Documents\\GitHub\\Organizador-de-Tarefas\\OrganizadorDeTarefas\\src\\testes\\dados.json")) {
            Gson gson = new Gson();
            gson.toJson(lista, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    
}



