const mock= [
    {
        "codigo": 253,
        "codigoUsuario": 46,
        "numero": "01",
        "nome": "Ativo",
        "valores": [],
        "nivelConta": 1
    },
    {
        "codigo": 254,
        "codigoUsuario": 46,
        "numero": "02",
        "nome": "Passivo",
        "valores": [],
        "nivelConta": 1
    },
    {
        "codigo": 255,
        "codigoUsuario": 46,
        "numero": "03",
        "nome": "Patrimônio Líquido",
        "valores": [],
        "nivelConta": 1
    },
    {
        "codigo": 256,
        "codigoUsuario": 46,
        "numero": "04",
        "nome": "Receita",
        "valores": [],
        "nivelConta": 1
    },
    {
        "codigo": 257,
        "codigoUsuario": 46,
        "numero": "05",
        "nome": "Despesas e Custos",
        "valores": [],
        "nivelConta": 1
    },
    {
        "codigo": 258,
        "contaPaiCodigo": 253,
        "codigoUsuario": 46,
        "numero": "01.01",
        "nome": "Circulante",
        "valores": [],
        "nivelConta": 2
    },
    {
        "codigo": 259,
        "contaPaiCodigo": 253,
        "codigoUsuario": 46,
        "numero": "01.02",
        "nome": "Não Circulante",
        "valores": [],
        "nivelConta": 2
    },
    {
        "codigo": 260,
        "contaPaiCodigo": 253,
        "codigoUsuario": 46,
        "numero": "01.03",
        "nome": "Realizavel a Longo Prazo",
        "valores": [],
        "nivelConta": 2
    },
    {
        "codigo": 261,
        "contaPaiCodigo": 253,
        "codigoUsuario": 46,
        "numero": "01.04",
        "nome": "Investimentos",
        "valores": [],
        "nivelConta": 2
    },
    {
        "codigo": 262,
        "contaPaiCodigo": 253,
        "codigoUsuario": 46,
        "numero": "01.05",
        "nome": "Imobilizado",
        "valores": [],
        "nivelConta": 2
    },
    {
        "codigo": 263,
        "contaPaiCodigo": 253,
        "codigoUsuario": 46,
        "numero": "01.06",
        "nome": "Intangível",
        "valores": [],
        "nivelConta": 2
    },
    {
        "codigo": 264,
        "contaPaiCodigo": 254,
        "codigoUsuario": 46,
        "numero": "02.01",
        "nome": "Circulante",
        "valores": [],
        "nivelConta": 2
    },
    {
        "codigo": 265,
        "contaPaiCodigo": 254,
        "codigoUsuario": 46,
        "numero": "02.02",
        "nome": "Não Circulante",
        "valores": [],
        "nivelConta": 2
    },
    {
        "codigo": 266,
        "contaPaiCodigo": 255,
        "codigoUsuario": 46,
        "numero": "03.01",
        "nome": "Capital",
        "valores": [
            {
                "codigo": 2,
                "tipo": "CREDITO",
                "codigoConta": 266,
                "data": 1591574400000,
                "historico": "Lançamento teste do usuário paulo",
                "saldoConta": 0.0,
                "valor": 100.0,
                "codigoLancamento": 17
            },
            {
                "codigo": 4,
                "tipo": "CREDITO",
                "codigoConta": 266,
                "data": 1591574400000,
                "historico": "Lançamento teste do usuário paulo",
                "saldoConta": 0.0,
                "valor": 100.0,
                "codigoLancamento": 18
            },
            {
                "codigo": 6,
                "tipo": "CREDITO",
                "codigoConta": 266,
                "data": 1591574400000,
                "historico": "Lançamento teste do usuário paulo",
                "saldoConta": 0.0,
                "valor": 100.0,
                "codigoLancamento": 19
            },
            {
                "codigo": 8,
                "tipo": "CREDITO",
                "codigoConta": 266,
                "data": 1591574400000,
                "historico": "Lançamento teste do usuário paulo",
                "saldoConta": 0.0,
                "valor": 100.0,
                "codigoLancamento": 20
            }
        ],
        "nivelConta": 2
    },
    {
        "codigo": 267,
        "contaPaiCodigo": 256,
        "codigoUsuario": 46,
        "numero": "04.01",
        "nome": "Receita Bruta",
        "valores": [],
        "nivelConta": 2
    },
    {
        "codigo": 268,
        "contaPaiCodigo": 256,
        "codigoUsuario": 46,
        "numero": "04.02",
        "nome": "Outras Receitas Operacionais",
        "valores": [],
        "nivelConta": 2
    },
    {
        "codigo": 269,
        "contaPaiCodigo": 257,
        "codigoUsuario": 46,
        "numero": "05.01",
        "nome": "Despesas",
        "valores": [],
        "nivelConta": 2
    },
    {
        "codigo": 270,
        "contaPaiCodigo": 257,
        "codigoUsuario": 46,
        "numero": "05.02",
        "nome": "Custos",
        "valores": [],
        "nivelConta": 2
    },
    {
        "codigo": 272,
        "contaPaiCodigo": 258,
        "codigoUsuario": 46,
        "numero": "01.01.01",
        "nome": "Caixa",
        "valores": [
            {
                "codigo": 1,
                "tipo": "DEBITO",
                "codigoConta": 272,
                "data": 1591574400000,
                "historico": "Lançamento teste do usuário paulo",
                "saldoConta": 0.0,
                "valor": 100.0,
                "codigoLancamento": 17
            },
            {
                "codigo": 3,
                "tipo": "DEBITO",
                "codigoConta": 272,
                "data": 1591574400000,
                "historico": "Lançamento teste do usuário paulo",
                "saldoConta": 0.0,
                "valor": 100.0,
                "codigoLancamento": 18
            },
            {
                "codigo": 5,
                "tipo": "DEBITO",
                "codigoConta": 272,
                "data": 1591574400000,
                "historico": "Lançamento teste do usuário paulo",
                "saldoConta": 0.0,
                "valor": 100.0,
                "codigoLancamento": 19
            },
            {
                "codigo": 7,
                "tipo": "DEBITO",
                "codigoConta": 272,
                "data": 1591574400000,
                "historico": "Lançamento teste do usuário paulo",
                "saldoConta": 0.0,
                "valor": 100.0,
                "codigoLancamento": 20
            }
        ],
        "nivelConta": 3
    },
    {
        "codigo": 273,
        "contaPaiCodigo": 258,
        "codigoUsuario": 46,
        "numero": "01.01.02",
        "nome": "Bancos",
        "valores": [],
        "nivelConta": 3
    }
];
export default class LoginService{

    public registerData(){
        localStorage.setItem("conta",   mock.toString());
    }
}