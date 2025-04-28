import kotlin.random.Random

// Modelo de Pokémon
data class Pokemon(
    val nome: String,
    val tipos: Map<String, List<String>>,
    var hp: Int,
    val ataque: Int,
    val defesa: Int,
    val velocidade: Int
)

// Modelo de Jogador
data class Jogador(
    val nickname: String,
    val time: List<Pokemon>
)

// Base de vantagens de tipos
val vantagensTipos = mapOf(
    "GRAMA" to listOf("AGUA", "TERRA", "ROCHA"),
    "VENENO" to listOf("GRAMA", "FADA"),
    "FOGO" to listOf("PLANTA", "GELO", "INSETO", "METAL"),
    "AGUA" to listOf("FOGO", "TERRA", "ROCHA"),
    "INSETO" to listOf("GRAMA", "PSIQUICO", "NOTURNO"),
    "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO"),
    "NORMAL" to listOf(),
    "ELETRICO" to listOf("AGUA", "VOADOR"),
    "TERRA" to listOf("FOGO", "ELETRICO", "VENENO", "ROCHA", "METAL"),
    "PSIQUICO" to listOf("LUTADOR", "VENENO"),
    "PEDRA" to listOf(),
    "GELO" to listOf("GRAMA", "TERRA", "VOADOR", "DRAGAO"),
    "FANTASMA" to listOf("PSIQUICO", "FANTASMA"),
    "FADA" to listOf("LUTADOR", "DRAGAO", "NOTURNO"),
    "METAL" to listOf("GELO", "ROCHA", "FADA"),
    "DRAGAO" to listOf("DRAGAO"),
    "LUTADOR" to listOf("NORMAL", "GELO", "PEDRA", "NOTURNO", "METAL")
)

// Verificar vantagem
fun temVantagem(tipoAtacante: String, tipoDefensor: String): Boolean {
    return vantagensTipos[tipoAtacante]?.contains(tipoDefensor.uppercase()) == true
}

// Calcular dano
fun calcularDano(atacante: Pokemon, defensor: Pokemon): Int {
    var ataqueAjustado = atacante.ataque.toDouble()
    var temVantagem = false
    for ((tipoAtacante, _) in atacante.tipos) {
        for ((tipoDefensor, _) in defensor.tipos) {
            if (temVantagem(tipoAtacante, tipoDefensor)) {
                temVantagem = true
            }
        }
    }
    if (temVantagem) {
        ataqueAjustado *= 1.5 // Multiplica apenas uma vez
    }
    var dano = ataqueAjustado - (defensor.defesa / 2.0)
    if (dano <= 0) dano = 1.0
    return dano.toInt()
}

// Batalha de Pokémon
fun batalhaPokemon(p1: Pokemon, p2: Pokemon): Pokemon {
    println("\n⚡ Batalha: ${p1.nome} (HP: ${p1.hp}) vs ${p2.nome} (HP: ${p2.hp})")

    var atacante = if (p1.velocidade >= p2.velocidade) p1 else p2
    var defensor = if (atacante == p1) p2 else p1

    while (p1.hp > 0 && p2.hp > 0) {
        val dano = calcularDano(atacante, defensor)
        defensor.hp -= dano
        println("${atacante.nome} atacou ${defensor.nome} causando $dano de dano! (HP de ${defensor.nome}: ${defensor.hp.coerceAtLeast(0)})")

        if (defensor.hp <= 0) {
            println("🏆 ${atacante.nome} venceu a batalha!\n")
            return atacante
        }

        // Troca de papéis
        val temp = atacante
        atacante = defensor
        defensor = temp
    }

    return atacante
}

// Batalha entre jogadores
fun batalhaJogadores(jogador1: Jogador, jogador2: Jogador) {
    println("\n🔥 Iniciando batalha entre ${jogador1.nickname} e ${jogador2.nickname}")

    var vitorias1 = 0
    var vitorias2 = 0

    for (i in jogador1.time.indices) {
        val poke1 = jogador1.time[i].copy()
        val poke2 = jogador2.time[i].copy()

        val vencedor = batalhaPokemon(poke1, poke2)
        if (vencedor.nome == poke1.nome) {
            vitorias1++
        } else {
            vitorias2++
        }
    }

    println("🎖️ Resultado Final 🎖️")
    println("${jogador1.nickname}: $vitorias1 vitória(s)")
    println("${jogador2.nickname}: $vitorias2 vitória(s)")

    when {
        vitorias1 > vitorias2 -> println("🏅 ${jogador1.nickname} é o campeão!")
        vitorias2 > vitorias1 -> println("🏅 ${jogador2.nickname} é o campeão!")
        else -> println("⚖️ Empate!")
    }
}

// Banco de Pokémons disponíveis

val pokemonsDisponiveis = listOf(
    Pokemon("Bulbasaur", mapOf("GRAMA" to listOf("AGUA", "TERRA", "ROCHA"), "VENENO" to listOf("GRAMA", "FADA")), 120, 52, 50, 45),
    Pokemon("Ivysaur", mapOf("GRAMA" to listOf("AGUA", "TERRA", "ROCHA"), "VENENO" to listOf("GRAMA", "FADA")), 130, 62, 60, 60),
    Pokemon("Venusaur", mapOf("GRAMA" to listOf("AGUA", "TERRA", "ROCHA"), "VENENO" to listOf("GRAMA", "FADA")), 150, 82, 80, 80),
    Pokemon("Charmander", mapOf("FOGO" to listOf("PLANTA", "GELO", "INSETO", "METAL")), 100, 60, 43, 65),
    Pokemon("Charmeleon", mapOf("FOGO" to listOf("PLANTA", "GELO", "INSETO", "METAL")), 120, 70, 58, 80),
    Pokemon("Charizard", mapOf("FOGO" to listOf("PLANTA", "GELO", "INSETO", "METAL"), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 140, 84, 78, 100),
    Pokemon("Squirtle", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 110, 48, 65, 43),
    Pokemon("Wartortle", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 125, 58, 80, 58),
    Pokemon("Blastoise", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 145, 78, 100, 78),
    Pokemon("Caterpie", mapOf("INSETO" to listOf("GRAMA", "PSIQUICO", "NOTURNO")), 90, 40, 35, 45),
    Pokemon("Metapod", mapOf("INSETO" to listOf("GRAMA", "PSIQUICO", "NOTURNO")), 100, 20, 55, 30),
    Pokemon("Butterfree", mapOf("INSETO" to listOf("GRAMA", "PSIQUICO", "NOTURNO"), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 120, 60, 50, 70),
    Pokemon("Weedle", mapOf("INSETO" to listOf("GRAMA", "PSIQUICO", "NOTURNO"), "VENENO" to listOf("GRAMA", "FADA")), 90, 45, 30, 50),
    Pokemon("Kakuna", mapOf("INSETO" to listOf("GRAMA", "PSIQUICO", "NOTURNO"), "VENENO" to listOf("GRAMA", "FADA")), 100, 25, 50, 35),
    Pokemon("Beedrill", mapOf("INSETO" to listOf("GRAMA", "PSIQUICO", "NOTURNO"), "VENENO" to listOf("GRAMA", "FADA")), 120, 80, 40, 75),
    Pokemon("Pidgey", mapOf("NORMAL" to listOf(), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 95, 45, 40, 56),
    Pokemon("Pidgeotto", mapOf("NORMAL" to listOf(), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 115, 60, 55, 71),
    Pokemon("Pidgeot", mapOf("NORMAL" to listOf(), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 135, 80, 75, 101),
    Pokemon("Rattata", mapOf("NORMAL" to listOf()), 90, 56, 35, 72),
    Pokemon("Raticate", mapOf("NORMAL" to listOf()), 110, 81, 60, 97),
    Pokemon("Spearow", mapOf("NORMAL" to listOf(), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 90, 60, 30, 70),
    Pokemon("Fearow", mapOf("NORMAL" to listOf(), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 120, 90, 65, 100),
    Pokemon("Ekans", mapOf("VENENO" to listOf("GRAMA", "FADA")), 95, 60, 44, 55),
    Pokemon("Arbok", mapOf("VENENO" to listOf("GRAMA", "FADA")), 120, 85, 69, 80),
    Pokemon("Pikachu", mapOf("ELETRICO" to listOf("AGUA", "VOADOR")), 90, 55, 40, 90),
    Pokemon("Raichu", mapOf("ELETRICO" to listOf("AGUA", "VOADOR")), 120, 90, 55, 110),
    Pokemon("Sandshrew", mapOf("TERRA" to listOf("FOGO", "ELETRICO", "VENENO", "ROCHA", "METAL")), 100, 75, 85, 40),
    Pokemon("Sandslash", mapOf("TERRA" to listOf("FOGO", "ELETRICO", "VENENO", "ROCHA", "METAL")), 125, 100, 110, 65),
    Pokemon("Nidoran♀", mapOf("VENENO" to listOf("GRAMA", "FADA")), 100, 47, 52, 41),
    Pokemon("Nidorina", mapOf("VENENO" to listOf("GRAMA", "FADA")), 120, 62, 67, 56),
    Pokemon("Nidoqueen", mapOf("VENENO" to listOf("GRAMA", "FADA"), "TERRA" to listOf("FOGO", "ELETRICO", "VENENO", "ROCHA", "METAL")), 140, 82, 87, 76),
    Pokemon("Nidoran♂", mapOf("VENENO" to listOf("GRAMA", "FADA")), 95, 57, 40, 50),
    Pokemon("Nidorino", mapOf("VENENO" to listOf("GRAMA", "FADA")), 115, 72, 57, 65),
    Pokemon("Nidoking", mapOf("VENENO" to listOf("GRAMA", "FADA"), "TERRA" to listOf("FOGO", "ELETRICO", "VENENO", "ROCHA", "METAL")), 135, 92, 77, 85),
    Pokemon("Clefairy", mapOf("FADA" to listOf("LUTADOR", "DRAGAO", "NOTURNO")), 110, 45, 48, 35),
    Pokemon("Clefable", mapOf("FADA" to listOf("LUTADOR", "DRAGAO", "NOTURNO")), 135, 70, 73, 60),
    Pokemon("Vulpix", mapOf("FOGO" to listOf("PLANTA", "GELO", "INSETO", "METAL")), 95, 41, 40, 65),
    Pokemon("Ninetales", mapOf("FOGO" to listOf("PLANTA", "GELO", "INSETO", "METAL")), 125, 76, 75, 100),
    Pokemon("Jigglypuff", mapOf("NORMAL" to listOf(), "FADA" to listOf("LUTADOR", "DRAGAO", "NOTURNO")), 140, 45, 20, 25),
    Pokemon("Wigglytuff", mapOf("NORMAL" to listOf(), "FADA" to listOf("LUTADOR", "DRAGAO", "NOTURNO")), 160, 70, 45, 50),
    Pokemon("Zubat", mapOf("VENENO" to listOf("GRAMA", "FADA"), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 90, 45, 35, 55),
    Pokemon("Golbat", mapOf("VENENO" to listOf("GRAMA", "FADA"), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 120, 80, 70, 90),
    Pokemon("Oddish", mapOf("GRAMA" to listOf("AGUA", "TERRA", "ROCHA"), "VENENO" to listOf("GRAMA", "FADA")), 95, 50, 55, 30),
    Pokemon("Gloom", mapOf("GRAMA" to listOf("AGUA", "TERRA", "ROCHA"), "VENENO" to listOf("GRAMA", "FADA")), 110, 65, 70, 40),
    Pokemon("Vileplume", mapOf("GRAMA" to listOf("AGUA", "TERRA", "ROCHA"), "VENENO" to listOf("GRAMA", "FADA")), 130, 80, 85, 50),
    Pokemon("Paras", mapOf("INSETO" to listOf("GRAMA", "PSIQUICO", "NOTURNO"), "GRAMA" to listOf("AGUA", "TERRA", "ROCHA")), 90, 70, 55, 25),
    Pokemon("Parasect", mapOf("INSETO" to listOf("GRAMA", "PSIQUICO", "NOTURNO"), "GRAMA" to listOf("AGUA", "TERRA", "ROCHA")), 120, 95, 80, 30),
    Pokemon("Venonat", mapOf("INSETO" to listOf("GRAMA", "PSIQUICO", "NOTURNO"), "VENENO" to listOf("GRAMA", "FADA")), 100, 55, 50, 45),
    Pokemon("Venomoth", mapOf("INSETO" to listOf("GRAMA", "PSIQUICO", "NOTURNO"), "VENENO" to listOf("GRAMA", "FADA")), 120, 65, 60, 90),
    Pokemon("Diglett", mapOf("TERRA" to listOf("FOGO", "ELETRICO", "VENENO", "ROCHA", "METAL")), 90, 55, 25, 95),
    Pokemon("Dugtrio", mapOf("TERRA" to listOf("FOGO", "ELETRICO", "VENENO", "ROCHA", "METAL")), 110, 80, 50, 120),
    Pokemon("Meowth", mapOf("NORMAL" to listOf()), 90, 45, 35, 90),
    Pokemon("Persian", mapOf("NORMAL" to listOf()), 120, 70, 60, 115),
    Pokemon("Psyduck", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 100, 52, 48, 55),
    Pokemon("Golduck", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 130, 82, 78, 85),
    Pokemon("Mankey", mapOf("LUTADOR" to listOf("NORMAL", "GELO", "PEDRA", "NOTURNO", "METAL")), 90, 80, 35, 70),
    Pokemon("Primeape", mapOf("LUTADOR" to listOf("NORMAL", "GELO", "PEDRA", "NOTURNO", "METAL")), 120, 105, 60, 95),
    Pokemon("Growlithe", mapOf("FOGO" to listOf("PLANTA", "GELO", "INSETO", "METAL")), 100, 70, 45, 60),
    Pokemon("Arcanine", mapOf("FOGO" to listOf("PLANTA", "GELO", "INSETO", "METAL")), 140, 110, 80, 95),
    Pokemon("Poliwag", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 90, 50, 40, 90),
    Pokemon("Poliwhirl", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 110, 65, 65, 90),
    Pokemon("Poliwrath", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA"), "LUTADOR" to listOf("NORMAL", "GELO", "PEDRA", "NOTURNO", "METAL")), 130, 85, 95, 70),
    Pokemon("Abra", mapOf("PSIQUICO" to listOf("LUTADOR", "VENENO")), 90, 20, 15, 90),
    Pokemon("Kadabra", mapOf("PSIQUICO" to listOf("LUTADOR", "VENENO")), 100, 35, 30, 105),
    Pokemon("Alakazam", mapOf("PSIQUICO" to listOf("LUTADOR", "VENENO")), 120, 50, 45, 120),
    Pokemon("Machop", mapOf("LUTADOR" to listOf("NORMAL", "GELO", "PEDRA", "NOTURNO", "METAL")), 100, 80, 50, 35),
    Pokemon("Machoke", mapOf("LUTADOR" to listOf("NORMAL", "GELO", "PEDRA", "NOTURNO", "METAL")), 120, 100, 70, 45),
    Pokemon("Machamp", mapOf("LUTADOR" to listOf("NORMAL", "GELO", "PEDRA", "NOTURNO", "METAL")), 140, 130, 80, 55),
    Pokemon("Bellsprout", mapOf("GRAMA" to listOf("AGUA", "TERRA", "ROCHA"), "VENENO" to listOf("GRAMA", "FADA")), 100, 75, 35, 40),
    Pokemon("Weepinbell", mapOf("GRAMA" to listOf("AGUA", "TERRA", "ROCHA"), "VENENO" to listOf("GRAMA", "FADA")), 115, 90, 50, 55),
    Pokemon("Victreebel", mapOf("GRAMA" to listOf("AGUA", "TERRA", "ROCHA"), "VENENO" to listOf("GRAMA", "FADA")), 130, 105, 65, 70),
    Pokemon("Tentacool", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA"), "VENENO" to listOf("GRAMA", "FADA")), 90, 40, 35, 70),
    Pokemon("Tentacruel", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA"), "VENENO" to listOf("GRAMA", "FADA")), 120, 70, 65, 100),
    Pokemon("Geodude", mapOf("ROCHA" to listOf(), "TERRA" to listOf("FOGO", "ELETRICO", "VENENO", "ROCHA", "METAL")), 90, 80, 100, 20),
    Pokemon("Graveler", mapOf("ROCHA" to listOf(), "TERRA" to listOf("FOGO", "ELETRICO", "VENENO", "ROCHA", "METAL")), 110, 95, 115, 35),
    Pokemon("Golem", mapOf("ROCHA" to listOf(), "TERRA" to listOf("FOGO", "ELETRICO", "VENENO", "ROCHA", "METAL")), 130, 110, 130, 45),
    Pokemon("Ponyta", mapOf("FOGO" to listOf("PLANTA", "GELO", "INSETO", "METAL")), 100, 85, 55, 90),
    Pokemon("Rapidash", mapOf("FOGO" to listOf("PLANTA", "GELO", "INSETO", "METAL")), 120, 100, 70, 105),
    Pokemon("Slowpoke", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA"), "PSIQUICO" to listOf("LUTADOR", "VENENO")), 140, 65, 65, 15),
    Pokemon("Slowbro", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA"), "PSIQUICO" to listOf("LUTADOR", "VENENO")), 160, 75, 110, 30),
    Pokemon("Magnemite", mapOf("ELETRICO" to listOf("AGUA", "VOADOR"), "METAL" to listOf("GELO", "ROCHA", "FADA")), 90, 35, 70, 45),
    Pokemon("Magneton", mapOf("ELETRICO" to listOf("AGUA", "VOADOR"), "METAL" to listOf("GELO", "ROCHA", "FADA")), 120, 60, 95, 70),
    Pokemon("Farfetch'd", mapOf("NORMAL" to listOf(), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 100, 65, 55, 60),
    Pokemon("Doduo", mapOf("NORMAL" to listOf(), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 90, 85, 45, 75),
    Pokemon("Dodrio", mapOf("NORMAL" to listOf(), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 120, 110, 70, 100),
    Pokemon("Seel", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 100, 45, 55, 45),
    Pokemon("Dewgong", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA"), "GELO" to listOf("GRAMA", "TERRA", "VOADOR", "DRAGAO")), 130, 70, 80, 70),
    Pokemon("Grimer", mapOf("VENENO" to listOf("GRAMA", "FADA")), 130, 80, 50, 25),
    Pokemon("Muk", mapOf("VENENO" to listOf("GRAMA", "FADA")), 160, 105, 75, 50),
    Pokemon("Shellder", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 90, 65, 100, 40),
    Pokemon("Cloyster", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA"), "GELO" to listOf("GRAMA", "TERRA", "VOADOR", "DRAGAO")), 120, 95, 180, 70),
    Pokemon("Gastly", mapOf("FANTASMA" to listOf("PSIQUICO", "FANTASMA"), "VENENO" to listOf("GRAMA", "FADA")), 90, 35, 30, 80),
    Pokemon("Haunter", mapOf("FANTASMA" to listOf("PSIQUICO", "FANTASMA"), "VENENO" to listOf("GRAMA", "FADA")), 100, 50, 45, 95),
    Pokemon("Gengar", mapOf("FANTASMA" to listOf("PSIQUICO", "FANTASMA"), "VENENO" to listOf("GRAMA", "FADA")), 120, 65, 60, 110),
    Pokemon("Onix", mapOf("ROCHA" to listOf(), "TERRA" to listOf("FOGO", "ELETRICO", "VENENO", "ROCHA", "METAL")), 90, 45, 160, 70),
    Pokemon("Drowzee", mapOf("PSIQUICO" to listOf("LUTADOR", "VENENO")), 100, 48, 45, 42),
    Pokemon("Hypno", mapOf("PSIQUICO" to listOf("LUTADOR", "VENENO")), 130, 73, 70, 67),
    Pokemon("Krabby", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 90, 105, 90, 50),
    Pokemon("Kingler", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 120, 130, 115, 75),
    Pokemon("Voltorb", mapOf("ELETRICO" to listOf("AGUA", "VOADOR")), 90, 30, 50, 100),
    Pokemon("Electrode", mapOf("ELETRICO" to listOf("AGUA", "VOADOR")), 120, 50, 70, 140),
    Pokemon("Exeggcute", mapOf("GRAMA" to listOf("AGUA", "TERRA", "ROCHA"), "PSIQUICO" to listOf("LUTADOR", "VENENO")), 100, 40, 80, 40),
    Pokemon("Exeggutor", mapOf("GRAMA" to listOf("AGUA", "TERRA", "ROCHA"), "PSIQUICO" to listOf("LUTADOR", "VENENO")), 140, 95, 85, 55),
    Pokemon("Cubone", mapOf("TERRA" to listOf("FOGO", "ELETRICO", "VENENO", "ROCHA", "METAL")), 100, 50, 95, 35),
    Pokemon("Marowak", mapOf("TERRA" to listOf("FOGO", "ELETRICO", "VENENO", "ROCHA", "METAL")), 120, 80, 110, 45),
    Pokemon("Hitmonlee", mapOf("LUTADOR" to listOf("NORMAL", "GELO", "PEDRA", "NOTURNO", "METAL")), 120, 120, 53, 87),
    Pokemon("Hitmonchan", mapOf("LUTADOR" to listOf("NORMAL", "GELO", "PEDRA", "NOTURNO", "METAL")), 120, 105, 79, 76),
    Pokemon("Lickitung", mapOf("NORMAL" to listOf()), 140, 55, 75, 30),
    Pokemon("Koffing", mapOf("VENENO" to listOf("GRAMA", "FADA")), 90, 65, 95, 35),
    Pokemon("Weezing", mapOf("VENENO" to listOf("GRAMA", "FADA")), 120, 90, 120, 60),
    Pokemon("Rhyhorn", mapOf("TERRA" to listOf("FOGO", "ELETRICO", "VENENO", "ROCHA", "METAL"), "ROCHA" to listOf()), 130, 85, 95, 25),
    Pokemon("Rhydon", mapOf("TERRA" to listOf("FOGO", "ELETRICO", "VENENO", "ROCHA", "METAL"), "ROCHA" to listOf()), 160, 130, 120, 40),
    Pokemon("Chansey", mapOf("NORMAL" to listOf()), 250, 5, 5, 50),
    Pokemon("Tangela", mapOf("GRAMA" to listOf("AGUA", "TERRA", "ROCHA")), 120, 55, 115, 60),
    Pokemon("Kangaskhan", mapOf("NORMAL" to listOf()), 140, 95, 80, 90),
    Pokemon("Horsea", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 90, 40, 70, 60),
    Pokemon("Seadra", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 120, 65, 95, 85),
    Pokemon("Goldeen", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 100, 67, 60, 63),
    Pokemon("Seaking", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 130, 92, 65, 68),
    Pokemon("Staryu", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 90, 45, 55, 85),
    Pokemon("Starmie", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA"), "PSIQUICO" to listOf("LUTADOR", "VENENO")), 120, 75, 85, 115),
    Pokemon("Mr. Mime", mapOf("PSIQUICO" to listOf("LUTADOR", "VENENO"), "FADA" to listOf("LUTADOR", "DRAGAO", "NOTURNO")), 100, 45, 65, 90),
    Pokemon("Scyther", mapOf("INSETO" to listOf("GRAMA", "PSIQUICO", "NOTURNO"), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 120, 110, 80, 105),
    Pokemon("Jynx", mapOf("GELO" to listOf("GRAMA", "TERRA", "VOADOR", "DRAGAO"), "PSIQUICO" to listOf("LUTADOR", "VENENO")), 120, 50, 35, 95),
    Pokemon("Electabuzz", mapOf("ELETRICO" to listOf("AGUA", "VOADOR")), 120, 83, 57, 105),
    Pokemon("Magmar", mapOf("FOGO" to listOf("PLANTA", "GELO", "INSETO", "METAL")), 120, 95, 57, 93),
    Pokemon("Pinsir", mapOf("INSETO" to listOf("GRAMA", "PSIQUICO", "NOTURNO")), 120, 125, 100, 85),
    Pokemon("Tauros", mapOf("NORMAL" to listOf()), 130, 100, 95, 110),
    Pokemon("Magikarp", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 90, 10, 55, 80),
    Pokemon("Gyarados", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA"), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 140, 125, 79, 81),
    Pokemon("Lapras", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA"), "GELO" to listOf("GRAMA", "TERRA", "VOADOR", "DRAGAO")), 160, 85, 80, 60),
    Pokemon("Ditto", mapOf("NORMAL" to listOf()), 100, 48, 48, 48),
    Pokemon("Eevee", mapOf("NORMAL" to listOf()), 100, 55, 50, 55),
    Pokemon("Vaporeon", mapOf("AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 160, 65, 60, 65),
    Pokemon("Jolteon", mapOf("ELETRICO" to listOf("AGUA", "VOADOR")), 120, 65, 60, 130),
    Pokemon("Flareon", mapOf("FOGO" to listOf("PLANTA", "GELO", "INSETO", "METAL")), 120, 130, 60, 65),
    Pokemon("Porygon", mapOf("NORMAL" to listOf()), 120, 60, 70, 40),
    Pokemon("Omanyte", mapOf("ROCHA" to listOf(), "AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 90, 40, 100, 35),
    Pokemon("Omastar", mapOf("ROCHA" to listOf(), "AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 120, 60, 125, 55),
    Pokemon("Kabuto", mapOf("ROCHA" to listOf(), "AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 90, 80, 90, 55),
    Pokemon("Kabutops", mapOf("ROCHA" to listOf(), "AGUA" to listOf("FOGO", "TERRA", "ROCHA")), 120, 115, 105, 80),
    Pokemon("Aerodactyl", mapOf("ROCHA" to listOf(), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 130, 105, 65, 130),
    Pokemon("Snorlax", mapOf("NORMAL" to listOf()), 200, 110, 65, 30),
    Pokemon("Articuno", mapOf("GELO" to listOf("GRAMA", "TERRA", "VOADOR", "DRAGAO"), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 140, 85, 100, 85),
    Pokemon("Zapdos", mapOf("ELETRICO" to listOf("AGUA", "VOADOR"), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 140, 90, 85, 100),
    Pokemon("Moltres", mapOf("FOGO" to listOf("PLANTA", "GELO", "INSETO", "METAL"), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 140, 100, 90, 90),
    Pokemon("Dratini", mapOf("DRAGAO" to listOf("DRAGAO")), 100, 64, 45, 50),
    Pokemon("Dragonair", mapOf("DRAGAO" to listOf("DRAGAO")), 120, 84, 65, 70),
    Pokemon("Dragonite", mapOf("DRAGAO" to listOf("DRAGAO"), "VOADOR" to listOf("GRAMA", "LUTADOR", "INSETO")), 140, 134, 95, 80),
    Pokemon("Mewtwo", mapOf("PSIQUICO" to listOf("LUTADOR", "VENENO")), 150, 110, 90, 130),
    Pokemon("Mew", mapOf("PSIQUICO" to listOf("LUTADOR", "VENENO")), 150, 100, 100, 100)
)
    // Adicione mais Pokémons se quiser!


// Função para escolher Pokémons
fun escolherTime(nomeJogador: String): Jogador {
    println("\n👤 $nomeJogador, escolha seu time (3 Pokémon):")

    val timeEscolhido = mutableListOf<Pokemon>()

    pokemonsDisponiveis.forEachIndexed { index, pokemon ->
        println("${index + 1}. ${pokemon.nome}")
    }

    while (timeEscolhido.size < 3) {
        print("Escolha o número do Pokémon ${timeEscolhido.size + 1}: ")
        val escolha = readLine()?.toIntOrNull()

        if (escolha != null && escolha in 1..pokemonsDisponiveis.size) {
            timeEscolhido.add(pokemonsDisponiveis[escolha - 1])
        } else {
            println("❌ Escolha inválida! Tente novamente.")
        }
    }

    return Jogador(nomeJogador, timeEscolhido)
}

// MAIN
fun main() {
    println("🎮 Bem-vindo ao Pokémon Kotlin Battle!")

    print("Digite o nickname do Jogador 1: ")
    val nickname1 = readLine() ?: "Jogador1"

    print("Digite o nickname do Jogador 2: ")
    val nickname2 = readLine() ?: "Jogador2"

    val jogador1 = escolherTime(nickname1)
    val jogador2 = escolherTime(nickname2)

    batalhaJogadores(jogador1, jogador2)
}
