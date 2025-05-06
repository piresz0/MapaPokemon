import kotlin.random.Random

// Modelo de Pokémon
data class Pokemon(
    val nome: String,
    val tipos: Map<String, String>, // Tipos e suas vantagens como string delimitada (ex.: "AGUA,TERRA,ROCHA")
    var hp: Int,
    val ataque: Int,
    val defesa: Int,
    val velocidade: Int
)

// Modelo de Jogador
data class Jogador(
    val nickname: String,
    val pokemon1: Pokemon,
    val pokemon2: Pokemon,
    val pokemon3: Pokemon
)

// Base de vantagens de tipos
val vantagensTipos = mapOf(
    "GRAMA" to "AGUA,TERRA,ROCHA",
    "VENENO" to "GRAMA,FADA",
    "FOGO" to "PLANTA,GELO,INSETO,METAL",
    "AGUA" to "FOGO,TERRA,ROCHA",
    "INSETO" to "GRAMA,PSIQUICO,NOTURNO",
    "VOADOR" to "GRAMA,LUTADOR,INSETO",
    "NORMAL" to "",
    "ELETRICO" to "AGUA,VOADOR",
    "TERRA" to "FOGO,ELETRICO,VENENO,ROCHA,METAL",
    "PSIQUICO" to "LUTADOR,VENENO",
    "PEDRA" to "",
    "GELO" to "GRAMA,TERRA,VOADOR,DRAGAO",
    "FANTASMA" to "PSIQUICO,FANTASMA",
    "FADA" to "LUTADOR,DRAGAO,NOTURNO",
    "METAL" to "GELO,ROCHA,FADA",
    "DRAGAO" to "DRAGAO",
    "LUTADOR" to "NORMAL,GELO,PEDRA,NOTURNO,METAL"
)

// Verificar vantagem
fun temVantagem(tipoAtacante: String, tipoDefensor: String): Boolean {
    val vantagens = vantagensTipos[tipoAtacante] ?: return false
    return vantagens.split(",").contains(tipoDefensor.uppercase())
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
        ataqueAjustado *= 1.5
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

    // Batalha 1: pokemon1 vs pokemon1
    run {
        val poke1 = jogador1.pokemon1.copy()
        val poke2 = jogador2.pokemon1.copy()
        val vencedor = batalhaPokemon(poke1, poke2)
        if (vencedor.nome == poke1.nome) vitorias1++ else vitorias2++
    }

    // Batalha 2: pokemon2 vs pokemon2
    run {
        val poke1 = jogador1.pokemon2.copy()
        val poke2 = jogador2.pokemon2.copy()
        val vencedor = batalhaPokemon(poke1, poke2)
        if (vencedor.nome == poke1.nome) vitorias1++ else vitorias2++
    }

    // Batalha 3: pokemon3 vs pokemon3
    run {
        val poke1 = jogador1.pokemon3.copy()
        val poke2 = jogador2.pokemon3.copy()
        val vencedor = batalhaPokemon(poke1, poke2)
        if (vencedor.nome == poke1.nome) vitorias1++ else vitorias2++
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
val pokemonsDisponiveis = mapOf(
    1 to Pokemon("Bulbasaur", mapOf("GRAMA" to "AGUA,TERRA,ROCHA", "VENENO" to "GRAMA,FADA"), 120, 52, 50, 45),
    2 to Pokemon("Ivysaur", mapOf("GRAMA" to "AGUA,TERRA,ROCHA", "VENENO" to "GRAMA,FADA"), 130, 62, 60, 60),
    3 to Pokemon("Venusaur", mapOf("GRAMA" to "AGUA,TERRA,ROCHA", "VENENO" to "GRAMA,FADA"), 150, 82, 80, 80),
    4 to Pokemon("Charmander", mapOf("FOGO" to "PLANTA,GELO,INSETO,METAL"), 100, 60, 43, 65),
    5 to Pokemon("Charmeleon", mapOf("FOGO" to "PLANTA,GELO,INSETO,METAL"), 120, 70, 58, 80),
    6 to Pokemon("Charizard", mapOf("FOGO" to "PLANTA,GELO,INSETO,METAL", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 140, 84, 78, 100),
    7 to Pokemon("Squirtle", mapOf("AGUA" to "FOGO,TERRA,ROCHA"), 110, 48, 65, 43),
    8 to Pokemon("Wartortle", mapOf("AGUA" to "FOGO,TERRA,ROCHA"), 125, 58, 80, 58),
    9 to Pokemon("Blastoise", mapOf("AGUA" to "FOGO,TERRA,ROCHA"), 145, 78, 100, 78),
    10 to Pokemon("Caterpie", mapOf("INSETO" to "GRAMA,PSIQUICO,NOTURNO"), 90, 40, 35, 45),
    11 to Pokemon("Metapod", mapOf("INSETO" to "GRAMA,PSIQUICO,NOTURNO"), 100, 20, 55, 30),
    12 to Pokemon("Butterfree", mapOf("INSETO" to "GRAMA,PSIQUICO,NOTURNO", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 120, 60, 50, 70),
    13 to Pokemon("Weedle", mapOf("INSETO" to "GRAMA,PSIQUICO,NOTURNO", "VENENO" to "GRAMA,FADA"), 90, 45, 30, 50),
    14 to Pokemon("Kakuna", mapOf("INSETO" to "GRAMA,PSIQUICO,NOTURNO", "VENENO" to "GRAMA,FADA"), 100, 25, 50, 35),
    15 to Pokemon("Beedrill", mapOf("INSETO" to "GRAMA,PSIQUICO,NOTURNO", "VENENO" to "GRAMA,FADA"), 120, 80, 40, 75),
    16 to Pokemon("Pidgey", mapOf("NORMAL" to "", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 95, 45, 40, 56),
    17 to Pokemon("Pidgeotto", mapOf("NORMAL" to "", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 115, 60, 55, 71),
    18 to Pokemon("Pidgeot", mapOf("NORMAL" to "", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 135, 80, 75, 101),
    19 to Pokemon("Rattata", mapOf("NORMAL" to ""), 90, 56, 35, 72),
    20 to Pokemon("Raticate", mapOf("NORMAL" to ""), 110, 81, 60, 97),
    21 to Pokemon("Spearow", mapOf("NORMAL" to "", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 90, 60, 30, 70),
    22 to Pokemon("Fearow", mapOf("NORMAL" to "", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 120, 90, 65, 100),
    23 to Pokemon("Ekans", mapOf("VENENO" to "GRAMA,FADA"), 95, 60, 44, 55),
    24 to Pokemon("Arbok", mapOf("VENENO" to "GRAMA,FADA"), 120, 85, 69, 80),
    25 to Pokemon("Pikachu", mapOf("ELETRICO" to "AGUA,VOADOR"), 90, 55, 40, 90),
    26 to Pokemon("Raichu", mapOf("ELETRICO" to "AGUA,VOADOR"), 120, 90, 55, 110),
    27 to Pokemon("Sandshrew", mapOf("TERRA" to "FOGO,ELETRICO,VENENO,ROCHA,METAL"), 100, 75, 85, 40),
    28 to Pokemon("Sandslash", mapOf("TERRA" to "FOGO,ELETRICO,VENENO,ROCHA,METAL"), 125, 100, 110, 65),
    29 to Pokemon("Nidoran♀", mapOf("VENENO" to "GRAMA,FADA"), 100, 47, 52, 41),
    30 to Pokemon("Nidorina", mapOf("VENENO" to "GRAMA,FADA"), 120, 62, 67, 56),
    31 to Pokemon("Nidoqueen", mapOf("VENENO" to "GRAMA,FADA", "TERRA" to "FOGO,ELETRICO,VENENO,ROCHA,METAL"), 140, 82, 87, 76),
    32 to Pokemon("Nidoran♂", mapOf("VENENO" to "GRAMA,FADA"), 95, 57, 40, 50),
    33 to Pokemon("Nidorino", mapOf("VENENO" to "GRAMA,FADA"), 115, 72, 57, 65),
    34 to Pokemon("Nidoking", mapOf("VENENO" to "GRAMA,FADA", "TERRA" to "FOGO,ELETRICO,VENENO,ROCHA,METAL"), 135, 92, 77, 85),
    35 to Pokemon("Clefairy", mapOf("FADA" to "LUTADOR,DRAGAO,NOTURNO"), 110, 45, 48, 35),
    36 to Pokemon("Clefable", mapOf("FADA" to "LUTADOR,DRAGAO,NOTURNO"), 135, 70, 73, 60),
    37 to Pokemon("Vulpix", mapOf("FOGO" to "PLANTA,GELO,INSETO,METAL"), 95, 41, 40, 65),
    38 to Pokemon("Ninetales", mapOf("FOGO" to "PLANTA,GELO,INSETO,METAL"), 125, 76, 75, 100),
    39 to Pokemon("Jigglypuff", mapOf("NORMAL" to "", "FADA" to "LUTADOR,DRAGAO,NOTURNO"), 140, 45, 20, 25),
    40 to Pokemon("Wigglytuff", mapOf("NORMAL" to "", "FADA" to "LUTADOR,DRAGAO,NOTURNO"), 160, 70, 45, 50),
    41 to Pokemon("Zubat", mapOf("VENENO" to "GRAMA,FADA", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 90, 45, 35, 55),
    42 to Pokemon("Golbat", mapOf("VENENO" to "GRAMA,FADA", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 120, 80, 70, 90),
    43 to Pokemon("Oddish", mapOf("GRAMA" to "AGUA,TERRA,ROCHA", "VENENO" to "GRAMA,FADA"), 95, 50, 55, 30),
    44 to Pokemon("Gloom", mapOf("GRAMA" to "AGUA,TERRA,ROCHA", "VENENO" to "GRAMA,FADA"), 110, 65, 70, 40),
    45 to Pokemon("Vileplume", mapOf("GRAMA" to "AGUA,TERRA,ROCHA", "VENENO" to "GRAMA,FADA"), 130, 80, 85, 50),
    46 to Pokemon("Paras", mapOf("INSETO" to "GRAMA,PSIQUICO,NOTURNO", "GRAMA" to "AGUA,TERRA,ROCHA"), 90, 70, 55, 25),
    47 to Pokemon("Parasect", mapOf("INSETO" to "GRAMA,PSIQUICO,NOTURNO", "GRAMA" to "AGUA,TERRA,ROCHA"), 120, 95, 80, 30),
    48 to Pokemon("Venonat", mapOf("INSETO" to "GRAMA,PSIQUICO,NOTURNO", "VENENO" to "GRAMA,FADA"), 100, 55, 50, 45),
    49 to Pokemon("Venomoth", mapOf("INSETO" to "GRAMA,PSIQUICO,NOTURNO", "VENENO" to "GRAMA,FADA"), 120, 65, 60, 90),
    50 to Pokemon("Diglett", mapOf("TERRA" to "FOGO,ELETRICO,VENENO,ROCHA,METAL"), 90, 55, 25, 95),
    51 to Pokemon("Dugtrio", mapOf("TERRA" to "FOGO,ELETRICO,VENENO,ROCHA,METAL"), 110, 80, 50, 120),
    52 to Pokemon("Meowth", mapOf("NORMAL" to ""), 90, 45, 35, 90),
    53 to Pokemon("Persian", mapOf("NORMAL" to ""), 120, 70, 60, 115),
    54 to Pokemon("Psyduck", mapOf("AGUA" to "FOGO,TERRA,ROCHA"), 100, 52, 48, 55),
    55 to Pokemon("Golduck", mapOf("AGUA" to "FOGO,TERRA,ROCHA"), 130, 82, 78, 85),
    56 to Pokemon("Mankey", mapOf("LUTADOR" to "NORMAL,GELO,PEDRA,NOTURNO,METAL"), 90, 80, 35, 70),
    57 to Pokemon("Primeape", mapOf("LUTADOR" to "NORMAL,GELO,PEDRA,NOTURNO,METAL"), 120, 105, 60, 95),
    58 to Pokemon("Growlithe", mapOf("FOGO" to "PLANTA,GELO,INSETO,METAL"), 100, 70, 45, 60),
    59 to Pokemon("Arcanine", mapOf("FOGO" to "PLANTA,GELO,INSETO,METAL"), 140, 110, 80, 95),
    60 to Pokemon("Poliwag", mapOf("AGUA" to "FOGO,TERRA,ROCHA"), 90, 50, 40, 90),
    61 to Pokemon("Poliwhirl", mapOf("AGUA" to "FOGO,TERRA,ROCHA"), 110, 65, 65, 90),
    62 to Pokemon("Poliwrath", mapOf("AGUA" to "FOGO,TERRA,ROCHA", "LUTADOR" to "NORMAL,GELO,PEDRA,NOTURNO,METAL"), 130, 85, 95, 70),
    63 to Pokemon("Abra", mapOf("PSIQUICO" to "LUTADOR,VENENO"), 90, 20, 15, 90),
    64 to Pokemon("Kadabra", mapOf("PSIQUICO" to "LUTADOR,VENENO"), 100, 35, 30, 105),
    65 to Pokemon("Alakazam", mapOf("PSIQUICO" to "LUTADOR,VENENO"), 120, 50, 45, 120),
    66 to Pokemon("Machop", mapOf("LUTADOR" to "NORMAL,GELO,PEDRA,NOTURNO,METAL"), 100, 80, 50, 35),
    67 to Pokemon("Machoke", mapOf("LUTADOR" to "NORMAL,GELO,PEDRA,NOTURNO,METAL"), 120, 100, 70, 45),
    68 to Pokemon("Machamp", mapOf("LUTADOR" to "NORMAL,GELO,PEDRA,NOTURNO,METAL"), 140, 130, 80, 55),
    69 to Pokemon("Bellsprout", mapOf("GRAMA" to "AGUA,TERRA,ROCHA", "VENENO" to "GRAMA,FADA"), 100, 75, 35, 40),
    70 to Pokemon("Weepinbell", mapOf("GRAMA" to "AGUA,TERRA,ROCHA", "VENENO" to "GRAMA,FADA"), 115, 90, 50, 55),
    71 to Pokemon("Victreebel", mapOf("GRAMA" to "AGUA,TERRA,ROCHA", "VENENO" to "GRAMA,FADA"), 130, 105, 65, 70),
    72 to Pokemon("Tentacool", mapOf("AGUA" to "FOGO,TERRA,ROCHA", "VENENO" to "GRAMA,FADA"), 90, 40, 35, 70),
    73 to Pokemon("Tentacruel", mapOf("AGUA" to "FOGO,TERRA,ROCHA", "VENENO" to "GRAMA,FADA"), 120, 70, 65, 100),
    74 to Pokemon("Geodude", mapOf("ROCHA" to "", "TERRA" to "FOGO,ELETRICO,VENENO,ROCHA,METAL"), 90, 80, 100, 20),
    75 to Pokemon("Graveler", mapOf("ROCHA" to "", "TERRA" to "FOGO,ELETRICO,VENENO,ROCHA,METAL"), 110, 95, 115, 35),
    76 to Pokemon("Golem", mapOf("ROCHA" to "", "TERRA" to "FOGO,ELETRICO,VENENO,ROCHA,METAL"), 130, 110, 130, 45),
    77 to Pokemon("Ponyta", mapOf("FOGO" to "PLANTA,GELO,INSETO,METAL"), 100, 85, 55, 90),
    78 to Pokemon("Rapidash", mapOf("FOGO" to "PLANTA,GELO,INSETO,METAL"), 120, 100, 70, 105),
    79 to Pokemon("Slowpoke", mapOf("AGUA" to "FOGO,TERRA,ROCHA", "PSIQUICO" to "LUTADOR,VENENO"), 140, 65, 65, 15),
    80 to Pokemon("Slowbro", mapOf("AGUA" to "FOGO,TERRA,ROCHA", "PSIQUICO" to "LUTADOR,VENENO"), 160, 75, 110, 30),
    81 to Pokemon("Magnemite", mapOf("ELETRICO" to "AGUA,VOADOR", "METAL" to "GELO,ROCHA,FADA"), 90, 35, 70, 45),
    82 to Pokemon("Magneton", mapOf("ELETRICO" to "AGUA,VOADOR", "METAL" to "GELO,ROCHA,FADA"), 120, 60, 95, 70),
    83 to Pokemon("Farfetch'd", mapOf("NORMAL" to "", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 100, 65, 55, 60),
    84 to Pokemon("Doduo", mapOf("NORMAL" to "", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 90, 85, 45, 75),
    85 to Pokemon("Dodrio", mapOf("NORMAL" to "", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 120, 110, 70, 100),
    86 to Pokemon("Seel", mapOf("AGUA" to "FOGO,TERRA,ROCHA"), 100, 45, 55, 45),
    87 to Pokemon("Dewgong", mapOf("AGUA" to "FOGO,TERRA,ROCHA", "GELO" to "GRAMA,TERRA,VOADOR,DRAGAO"), 130, 70, 80, 70),
    88 to Pokemon("Grimer", mapOf("VENENO" to "GRAMA,FADA"), 130, 80, 50, 25),
    89 to Pokemon("Muk", mapOf("VENENO" to "GRAMA,FADA"), 160, 105, 75, 50),
    90 to Pokemon("Shellder", mapOf("AGUA" to "FOGO,TERRA,ROCHA"), 90, 65, 100, 40),
    91 to Pokemon("Cloyster", mapOf("AGUA" to "FOGO,TERRA,ROCHA", "GELO" to "GRAMA,TERRA,VOADOR,DRAGAO"), 120, 95, 180, 70),
    92 to Pokemon("Gastly", mapOf("FANTASMA" to "PSIQUICO,FANTASMA", "VENENO" to "GRAMA,FADA"), 90, 35, 30, 80),
    93 to Pokemon("Haunter", mapOf("FANTASMA" to "PSIQUICO,FANTASMA", "VENENO" to "GRAMA,FADA"), 100, 50, 45, 95),
    94 to Pokemon("Gengar", mapOf("FANTASMA" to "PSIQUICO,FANTASMA", "VENENO" to "GRAMA,FADA"), 120, 65, 60, 110),
    95 to Pokemon("Onix", mapOf("ROCHA" to "", "TERRA" to "FOGO,ELETRICO,VENENO,ROCHA,METAL"), 90, 45, 160, 70),
    96 to Pokemon("Drowzee", mapOf("PSIQUICO" to "LUTADOR,VENENO"), 100, 48, 45, 42),
    97 to Pokemon("Hypno", mapOf("PSIQUICO" to "LUTADOR,VENENO"), 130, 73, 70, 67),
    98 to Pokemon("Krabby", mapOf("AGUA" to "FOGO,TERRA,ROCHA"), 90, 105, 90, 50),
    99 to Pokemon("Kingler", mapOf("AGUA" to "FOGO,TERRA,ROCHA"), 120, 130, 115, 75),
    100 to Pokemon("Voltorb", mapOf("ELETRICO" to "AGUA,VOADOR"), 90, 30, 50, 100),
    101 to Pokemon("Electrode", mapOf("ELETRICO" to "AGUA,VOADOR"), 120, 50, 70, 140),
    102 to Pokemon("Exeggcute", mapOf("GRAMA" to "AGUA,TERRA,ROCHA", "PSIQUICO" to "LUTADOR,VENENO"), 100, 40, 80, 40),
    103 to Pokemon("Exeggutor", mapOf("GRAMA" to "AGUA,TERRA,ROCHA", "PSIQUICO" to "LUTADOR,VENENO"), 140, 95, 85, 55),
    104 to Pokemon("Cubone", mapOf("TERRA" to "FOGO,ELETRICO,VENENO,ROCHA,METAL"), 100, 50, 95, 35),
    105 to Pokemon("Marowak", mapOf("TERRA" to "FOGO,ELETRICO,VENENO,ROCHA,METAL"), 120, 80, 110, 45),
    106 to Pokemon("Hitmonlee", mapOf("LUTADOR" to "NORMAL,GELO,PEDRA,NOTURNO,METAL"), 120, 120, 53, 87),
    107 to Pokemon("Hitmonchan", mapOf("LUTADOR" to "NORMAL,GELO,PEDRA,NOTURNO,METAL"), 120, 105, 79, 76),
    108 to Pokemon("Lickitung", mapOf("NORMAL" to ""), 140, 55, 75, 30),
    109 to Pokemon("Koffing", mapOf("VENENO" to "GRAMA,FADA"), 90, 65, 95, 35),
    110 to Pokemon("Weezing", mapOf("VENENO" to "GRAMA,FADA"), 120, 90, 120, 60),
    111 to Pokemon("Rhyhorn", mapOf("TERRA" to "FOGO,ELETRICO,VENENO,ROCHA,METAL", "ROCHA" to ""), 130, 85, 95, 25),
    112 to Pokemon("Rhydon", mapOf("TERRA" to "FOGO,ELETRICO,VENENO,ROCHA,METAL", "ROCHA" to ""), 160, 130, 120, 40),
    113 to Pokemon("Chansey", mapOf("NORMAL" to ""), 250, 5, 5, 50),
    114 to Pokemon("Tangela", mapOf("GRAMA" to "AGUA,TERRA,ROCHA"), 120, 55, 115, 60),
    115 to Pokemon("Kangaskhan", mapOf("NORMAL" to ""), 140, 95, 80, 90),
    116 to Pokemon("Horsea", mapOf("AGUA" to "FOGO,TERRA,ROCHA"), 90, 40, 70, 60),
    117 to Pokemon("Seadra", mapOf("AGUA" to "FOGO,TERRA,ROCHA"), 120, 65, 95, 85),
    118 to Pokemon("Goldeen", mapOf("AGUA" to "FOGO,TERRA,ROCHA"), 100, 67, 60, 63),
    119 to Pokemon("Seaking", mapOf("AGUA" to "FOGO,TERRA,ROCHA"), 130, 92, 65, 68),
    120 to Pokemon("Staryu", mapOf("AGUA" to "FOGO,TERRA,ROCHA"), 90, 45, 55, 85),
    121 to Pokemon("Starmie", mapOf("AGUA" to "FOGO,TERRA,ROCHA", "PSIQUICO" to "LUTADOR,VENENO"), 120, 75, 85, 115),
    122 to Pokemon("Mr. Mime", mapOf("PSIQUICO" to "LUTADOR,VENENO", "FADA" to "LUTADOR,DRAGAO,NOTURNO"), 100, 45, 65, 90),
    123 to Pokemon("Scyther", mapOf("INSETO" to "GRAMA,PSIQUICO,NOTURNO", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 120, 110, 80, 105),
    124 to Pokemon("Jynx", mapOf("GELO" to "GRAMA,TERRA,VOADOR,DRAGAO", "PSIQUICO" to "LUTADOR,VENENO"), 120, 50, 35, 95),
    125 to Pokemon("Electabuzz", mapOf("ELETRICO" to "AGUA,VOADOR"), 120, 83, 57, 105),
    126 to Pokemon("Magmar", mapOf("FOGO" to "PLANTA,GELO,INSETO,METAL"), 120, 95, 57, 93),
    127 to Pokemon("Pinsir", mapOf("INSETO" to "GRAMA,PSIQUICO,NOTURNO"), 120, 125, 100, 85),
    128 to Pokemon("Tauros", mapOf("NORMAL" to ""), 130, 100, 95, 110),
    129 to Pokemon("Magikarp", mapOf("AGUA" to "FOGO,TERRA,ROCHA"), 90, 10, 55, 80),
    130 to Pokemon("Gyarados", mapOf("AGUA" to "FOGO,TERRA,ROCHA", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 140, 125, 79, 81),
    131 to Pokemon("Lapras", mapOf("AGUA" to "FOGO,TERRA,ROCHA", "GELO" to "GRAMA,TERRA,VOADOR,DRAGAO"), 160, 85, 80, 60),
    132 to Pokemon("Ditto", mapOf("NORMAL" to ""), 100, 48, 48, 48),
    133 to Pokemon("Eevee", mapOf("NORMAL" to ""), 100, 55, 50, 55),
    134 to Pokemon("Vaporeon", mapOf("AGUA" to "FOGO,TERRA,ROCHA"), 160, 65, 60, 65),
    135 to Pokemon("Jolteon", mapOf("ELETRICO" to "AGUA,VOADOR"), 120, 65, 60, 130),
    136 to Pokemon("Flareon", mapOf("FOGO" to "PLANTA,GELO,INSETO,METAL"), 120, 130, 60, 65),
    137 to Pokemon("Porygon", mapOf("NORMAL" to ""), 120, 60, 70, 40),
    138 to Pokemon("Omanyte", mapOf("ROCHA" to "", "AGUA" to "FOGO,TERRA,ROCHA"), 90, 40, 100, 35),
    139 to Pokemon("Omastar", mapOf("ROCHA" to "", "AGUA" to "FOGO,TERRA,ROCHA"), 120, 60, 125, 55),
    140 to Pokemon("Kabuto", mapOf("ROCHA" to "", "AGUA" to "FOGO,TERRA,ROCHA"), 90, 80, 90, 55),
    141 to Pokemon("Kabutops", mapOf("ROCHA" to "", "AGUA" to "FOGO,TERRA,ROCHA"), 120, 115, 105, 80),
    142 to Pokemon("Aerodactyl", mapOf("ROCHA" to "", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 130, 105, 65, 130),
    143 to Pokemon("Snorlax", mapOf("NORMAL" to ""), 200, 110, 65, 30),
    144 to Pokemon("Articuno", mapOf("GELO" to "GRAMA,TERRA,VOADOR,DRAGAO", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 140, 85, 100, 85),
    145 to Pokemon("Zapdos", mapOf("ELETRICO" to "AGUA,VOADOR", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 140, 90, 85, 100),
    146 to Pokemon("Moltres", mapOf("FOGO" to "PLANTA,GELO,INSETO,METAL", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 140, 100, 90, 90),
    147 to Pokemon("Dratini", mapOf("DRAGAO" to "DRAGAO"), 100, 64, 45, 50),
    148 to Pokemon("Dragonair", mapOf("DRAGAO" to "DRAGAO"), 120, 84, 65, 70),
    149 to Pokemon("Dragonite", mapOf("DRAGAO" to "DRAGAO", "VOADOR" to "GRAMA,LUTADOR,INSETO"), 140, 134, 95, 80),
    150 to Pokemon("Mewtwo", mapOf("PSIQUICO" to "LUTADOR,VENENO"), 150, 110, 90, 130),
    151 to Pokemon("Mew", mapOf("PSIQUICO" to "LUTADOR,VENENO"), 150, 100, 100, 100)
)

// Função para escolher Pokémons
fun escolherTime(nomeJogador: String): Jogador {
    println("\n👤 $nomeJogador, escolha seu time (3 Pokémon).")
    println("Digite o número do Pokémon (de 1 a 151). Exemplo: 1 para Bulbasaur, 25 para Pikachu.")

    var pokemon1: Pokemon? = null
    var pokemon2: Pokemon? = null
    var pokemon3: Pokemon? = null

    while (pokemon1 == null) {
        print("Digite o número do Pokémon 1: ")
        val escolha = readLine()?.trim()?.toIntOrNull()
        if (escolha != null && pokemonsDisponiveis.containsKey(escolha) && escolha in 1..151) {
            pokemon1 = pokemonsDisponiveis[escolha]
            println("Você escolheu ${pokemon1?.nome}")
        } else {
            println("❌ Número inválido! Digite um número entre 1 e 151, como 1 (Bulbasaur) ou 25 (Pikachu).")
        }
    }

    while (pokemon2 == null) {
        print("Digite o número do Pokémon 2: ")
        val escolha = readLine()?.trim()?.toIntOrNull()
        if (escolha != null && pokemonsDisponiveis.containsKey(escolha) && escolha in 1..151) {
            pokemon2 = pokemonsDisponiveis[escolha]
            println("Você escolheu ${pokemon2?.nome}")
        } else {
            println("❌ Número inválido! Digite um número entre 1 e 151, como 4 (Charmander) ou 7 (Squirtle).")
        }
    }

    while (pokemon3 == null) {
        print("Digite o número do Pokémon 3: ")
        val escolha = readLine()?.trim()?.toIntOrNull()
        if (escolha != null && pokemonsDisponiveis.containsKey(escolha) && escolha in 1..151) {
            pokemon3 = pokemonsDisponiveis[escolha]
            println("Você escolheu ${pokemon3?.nome}")
        } else {
            println("❌ Número inválido! Digite um número entre 1 e 151, como 6 (Charizard) ou 9 (Blastoise).")
        }
    }

    return Jogador(nomeJogador, pokemon1, pokemon2, pokemon3)
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
