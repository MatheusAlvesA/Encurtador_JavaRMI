<!DOCTYPE html>
<html lang="pt-br">
<head>
  <title>Encurtador</title>

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css">

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>

 <link rel="icon" type="image/png" href="https://i.imgur.com/FYYtJmJ.png">

</head>

<body>



<nav class="navbar navbar-expand-sm navbar-toggleable-md bg-dark navbar-dark">
	<h4 style="color: white;"><a href="http://encurtador.matheusalves.com.br" style="color: white;">Encurtador </a><small class="text-muted" style="font-size: 7pt;">1.0</small></h4>

	<ul class="navbar-nav ml-auto">
        <li class="nav-item">
			<a href="#" id="link_bntTempo" style="display: none;"><button type="button" class="btn btn-outline-success" id="bntTempo">Aguarde ? segundos</button></a>
        </li>
    </ul>
</nav>

<br>

<div class="container-fluid" style="display: none;" id="Propaganda">
	<div class="row">

		<div class="col-5">
	      <img src="https://images-americanas.b2w.io/produtos/01/00/sku/30478/0/30478054_1GG.jpg" class="img-fluid">
	      <div class="row">
	      	<div class="col-6">
	      		<img src="https://images-americanas.b2w.io/produtos/01/00/sku/30478/0/30478054_2GG.jpg" class="img-thumbnail float-left">
	      	</div>
	      	<div class="col-6">
	      		<img src="https://images-americanas.b2w.io/produtos/01/00/sku/30478/0/30478054_3GG.jpg" class="img-thumbnail float-right">
	      	</div>
	      </div>
	    </div>
	    <div class="col-7">
	      <h1 style='font-size:5vw; color: #C0392B; font-family: "Arial Black", Gadget, sans-serif'>Nintendo Switch</h1>
	      <hr>
			<div class="card" style="width: 50vw;">
			  <div class="card-body">
			    <h4 class="card-title">Por R$ 2.548.564,99</h4>
			    <h5 class="card-subtitle mb-2 text-muted" style="text-decoration: line-through">de R$ 4.867.674,99</h5>
			    <p class="card-text">É possível sincronizar até oito consoles o Nintendo Switch e participar em títulos com opções multijogadores em cooperação ou competição. Se subscreveres o serviço online da Nintendo Switch poderá jogar com amigos ou jogadores de qualquer parte do mundo.</p>
			    <a href="#" class="card-link"><button type="button" class="btn btn-danger">Comprar</button></a>
			    <a href="#" class="card-link"><button type="button" class="btn btn-outline-info">Lista de Desejos</button></a>
			  </div>
			</div>
	    </div>

	</div>
</div>

<div class="container-fluid" id="Encurtador">
	<div class="row">
	    <div class="col-md-6 offset-md-3">
	      <h1 style='font-size:5vw; color: #C0392B; font-family: "Arial Black", Gadget, sans-serif'>Encurtador</h1>
	      <hr>
	      	<form id="formulario" action="#">
				<div class="form-group">
					<input type="text" id="url" class="form-control" aria-describedby="ajuda" placeholder="URL a ser encurtada">
					<div id="msgERRO" class="invalid-feedback">Falha na conexão com o servidor</div>
					<small id="ajuda" class="form-text text-muted">
				 		Insira aqui a URL que você deseja encurtar e então precione <b>ENTER</b>
					</small>
				</div>
			</form>
			<div id="painelURLCurta" style="border: 1px dashed #AED6F1; display: none;">
				<b>Sua URL encurtada é: </b><i id="urlCurta" style="font-size: 15pt; text-decoration: underline;"></i>
			</div>
	    </div>
	</div>
</div>

<script type="text/javascript">
	$("#formulario").submit(function( event ) {
	  let urlLonga = $("#url").val();
	  encurtar(urlLonga);
	  event.preventDefault();
	});

	checarEncurtada(document.URL);

	function encurtar(urlLonga) {
		$.ajax({
		   type: "POST",
		   url: "http://encurtador.matheusalves.com.br/api",
		   dataType: "json",
		   data: JSON.stringify({ 
			"URL": urlLonga
			}),
		   success: function(result){
		       $("#painelURLCurta").css("display", "block")
		       $("#urlCurta").html(result.curta);
		       $("#url").val("");
		       $("#url").last().removeClass("is-invalid");
		   },
		    error: function(result) {
		    	$("#url").last().addClass("is-invalid");
		    	$("#msgERRO").html(result.responseJSON.mensagem);
		    }
		 });
	}

	function checarEncurtada(urlCurta) {
		$.ajax({
		   type: "GET",
		   url: "http://encurtador.matheusalves.com.br/api",
		   data: { 
			"URL": urlCurta
			},
		   success: function(result){
		       $("#Encurtador").css("display", "none")
		       $("#Propaganda").css("display", "block")
		       $("#link_bntTempo").css("display", "block")
		       iniciarContagem(result.longa);
		   },
		    error: function(result) {}
		 });
	}

	function iniciarContagem(urlLonga) {
		var segundos = 5;
		$("#bntTempo").html("Aguarde "+segundos+" segundos");
		var i = setInterval(function() {
					segundos--;
					$("#bntTempo").html("Aguarde "+segundos+" segundos");

					if(segundos <= 0) {
						clearInterval(i);
						if(urlLonga.substring(0, 4) != "http")
							urlLonga = "http://"+urlLonga;
						$("#link_bntTempo").attr("href", urlLonga); 
						$("#bntTempo").html("Prosseguir");
					}
				}, 1000);
	}

</script>

</body>
</html>