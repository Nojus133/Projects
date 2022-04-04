using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Server.Kestrel.Core;
using Microsoft.Extensions.Logging;
using Moq;
using oblig1.Controllers;
using oblig1.DAL;
using oblig1.Models;
using System;
using System.Collections.Generic;
using System.Net;
using System.Threading.Tasks;
using Xunit;

namespace UnitTest
{
    public class BestillingTest
    {
        private const string _loggetInn = "loggetInn";
        private const string _ikkeLoggetInn = "";

        private readonly Mock<IBussReiseRepository> mockRep = new Mock<IBussReiseRepository>();
        private readonly Mock<ILogger<BestillingController>> mockLog = new Mock<ILogger<BestillingController>>();

        private readonly Mock<HttpContext> mockHttpContext = new Mock<HttpContext>();
        private readonly MockHttpSession mockSession = new MockHttpSession();

        [Fact]
        public async Task LoggInnOK()
        {
            mockRep.Setup(k => k.LoggInn(It.IsAny<Bruker>())).ReturnsAsync(true);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            mockSession[_loggetInn] = _loggetInn;
            mockHttpContext.Setup(s => s.Session).Returns(mockSession);
            bestillingController.ControllerContext.HttpContext = mockHttpContext.Object;

            var resultat = await bestillingController.LoggInn(It.IsAny<Bruker>()) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.True((bool)resultat.Value);
        }

        [Fact]
        public async Task LoggInnFeilPassordEllerBruker()
        {
            mockRep.Setup(k => k.LoggInn(It.IsAny<Bruker>())).ReturnsAsync(false);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);

            mockSession[_loggetInn] = _ikkeLoggetInn;
            mockHttpContext.Setup(s => s.Session).Returns(mockSession);
            bestillingController.ControllerContext.HttpContext = mockHttpContext.Object;
            var resultat = await bestillingController.LoggInn(It.IsAny<Bruker>()) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.False((bool)resultat.Value);
        }

        [Fact]
        public async Task LoggInnInputFeil()
        {
            mockRep.Setup(k => k.LoggInn(It.IsAny<Bruker>())).ReturnsAsync(true);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            bestillingController.ModelState.AddModelError("Brukernavn", "Input validerings feil på server");

            mockSession[_loggetInn] = _loggetInn;
            mockHttpContext.Setup(s => s.Session).Returns(mockSession);
            bestillingController.ControllerContext.HttpContext = mockHttpContext.Object;

            var resultat = await bestillingController.LoggInn(It.IsAny<Bruker>()) as BadRequestObjectResult;

            Assert.Equal((int)HttpStatusCode.BadRequest, resultat.StatusCode);
            Assert.Equal("Input validerings feil på server", resultat.Value);
        }

        [Fact]
        public void LoggUt()
        {
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            mockHttpContext.Setup(s => s.Session).Returns(mockSession);
            mockSession[_loggetInn] = _loggetInn;
            bestillingController.ControllerContext.HttpContext = mockHttpContext.Object;

            bestillingController.LoggUt();

            Assert.Equal(_ikkeLoggetInn, mockSession[_loggetInn]);
        }

        [Fact]
        public async Task TestHentAlleBilletter()
        {
            var kunde1 = new Kunder
            {
                Epost = "perhansen@gmail.com",
                Telefon = "23425412"
            };

            var bestilling1 = new Bestillingen
            {
                BestillingsDato = DateTime.Today,
                TotaltPris = 200,
                Kunder = kunde1,
            };

            var type1 = new BillettType
            {
                Type = "voksen",
                Pris = 200
            };

            var fraSted = new Strekninger
            {
                StedNavn = "Oslo"
            };

            var tilSted = new Strekninger
            {
                StedNavn = "Bergen"
            };

            var tid1 = new Tidsplaner
            {
                ReiseTid = "09:00",
                Ukedag = 0
            };

            var rute1 = new Rute
            {
                StedNavn = fraSted,
                TId = tid1
            };

            var rute2 = new Rute
            {
                StedNavn = tilSted,
                TId = tid1
            };

            var billett1 = new EnkeltBillett
            {
                ReiseDato = DateTime.Today,
                Bestillinger = bestilling1,
                BillettTyper = type1,
                Fra = fraSted,
                Til = tilSted,
                Ruten = rute1
            };

            var billett2 = new EnkeltBillett
            {
                ReiseDato = DateTime.Today.AddDays(7),
                Bestillinger = bestilling1,
                BillettTyper = type1,
                Fra = tilSted,
                Til = fraSted,
                Ruten = rute1
            };

            var billettListe = new List<EnkeltBillett>();
            billettListe.Add(billett1);
            billettListe.Add(billett2);

            mockRep.Setup(rep => rep.HentAlleBilletter()).ReturnsAsync(billettListe);

            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.HentAlleBilletter() as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal<List<EnkeltBillett>>((List<EnkeltBillett>)resultat.Value, billettListe);
        }
        
        [Fact]
        public async Task TestHentAlleTomBilletter()
        {
            var billettListe = new List<EnkeltBillett>();

            mockRep.Setup(rep => rep.HentAlleBilletter()).ReturnsAsync(() => null); 

            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.HentAlleBilletter() as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke hente alle billettene", resultat.Value);
        }
        
        [Fact]
        public async Task TestHentRuter()
        {
            var sted1 = new Strekninger
            {
                StedNavn = "Oslo"
            };

            var sted2 = new Strekninger
            {
                StedNavn = "Bergen"
            };

            var tid1 = new Tidsplaner
            {
                ReiseTid = "09:00",
                Ukedag = 0
            };

            var tid2 = new Tidsplaner
            {
                ReiseTid = "12:00",
                Ukedag = 2
            };

            var rute1 = new Rute
            {
                StedNavn = sted1,
                TId = tid1
            };

            var rute2 = new Rute
            {
                StedNavn = sted2,
                TId = tid2
            };

            var ruteListe = new List<Rute>();
            ruteListe.Add(rute1);
            ruteListe.Add(rute2);

            mockRep.Setup(rep => rep.HentRuter()).ReturnsAsync(ruteListe);

            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.HentRuter() as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal<List<Rute>>((List<Rute>)resultat.Value, ruteListe);
        }
        
        [Fact]
        public async Task TestHentIngenRuter()
        {
            var ruteListe = new List<Rute>();

            mockRep.Setup(rep => rep.HentRuter()).ReturnsAsync(() => null);

            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.HentRuter() as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke hente alle rutene", resultat.Value);
        }
        
        [Fact]
        public async Task TestHentRutene()
        {
            var sted1 = new Strekninger
            {
                StedNavn = "Oslo"
            };

            var tid1 = new Tidsplaner
            {
                ReiseTid = "09:00",
                Ukedag = 0
            };

            var returRute = new Rute
            {
                Rute_Id = 1,
                StedNavn = sted1,
                TId = tid1
            };

            var ruteListe = new List<Rute>();
            ruteListe.Add(returRute);

            
            string arg = "Oslo";
            mockRep.Setup(rep => rep.HentRutene(arg)).ReturnsAsync(ruteListe);

            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.HentRutene(arg) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal<List<Rute>>((List<Rute>)resultat.Value, ruteListe);
        }
        
        [Fact]
        public async Task TestHentIngenRutene()
        {
            var ruteListe = new List<Rute>();

            string arg = "Oslo";
            mockRep.Setup(rep => rep.HentRutene(arg)).ReturnsAsync(()=>null);

            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.HentRutene(arg) as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke hente rutene", resultat.Value);
        }
        
        [Fact]
        public async Task TestHentTidsplaner()
        {
            var tid1 = new Tidsplaner
            {
                ReiseTid = "09:00",
                Ukedag = 0
            };

            var tid2 = new Tidsplaner
            {
                ReiseTid = "12:00",
                Ukedag = 2
            };

            var tidsplanerListe = new List<Tidsplaner>();
            tidsplanerListe.Add(tid1);
            tidsplanerListe.Add(tid1);

            mockRep.Setup(rep => rep.HentTidsplaner()).ReturnsAsync(tidsplanerListe);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.HentTidsplaner() as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal<List<Tidsplaner>>((List<Tidsplaner>)resultat.Value, tidsplanerListe);
        }

        [Fact]
        public async Task TestHentIngenTidsplaner()
        {
            var tidsplanerListe = new List<Tidsplaner>();

            mockRep.Setup(rep => rep.HentTidsplaner()).ReturnsAsync(() => null);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.HentTidsplaner() as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke hente tidsplaner", resultat.Value);
        }
        
        [Fact]
        public async Task TestHentBestillinger()
        {
            var kunde1 = new Kunder
            {
                Epost = "perhansen@gmail.com",
                Telefon = "23425412"
            };

            var bestilling1 = new Bestillingen
            {
                BestillingsDato = DateTime.Today,
                TotaltPris = 200,
                Kunder = kunde1,
            };

            var kunde2 = new Kunder
            {
                Epost = "oleolsen@gmail.com",
                Telefon = "33426412"
            };

            var bestilling2 = new Bestillingen
            {
                BestillingsDato = DateTime.Today,
                TotaltPris = 400,
                Kunder = kunde2,
            };

            var bestillingListe = new List<Bestillingen>();
            bestillingListe.Add(bestilling1);
            bestillingListe.Add(bestilling2);

            mockRep.Setup(rep => rep.HentBestillinger()).ReturnsAsync(bestillingListe);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.HentBestillinger() as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal<List<Bestillingen>>((List<Bestillingen>)resultat.Value, bestillingListe);
        }

        [Fact]
        public async Task TestHentIngenBestillinger()
        {
            var bestillingListe = new List<Bestillingen>();

            mockRep.Setup(rep => rep.HentBestillinger()).ReturnsAsync(()=>null);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.HentBestillinger() as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke hente bestillinger", resultat.Value);
        }
        
        [Fact]
        public async Task TestHentKunder()
        {
            var kunde1 = new Kunder
            {
                Epost = "perhansen@gmail.com",
                Telefon = "23425412"
            };

            var kunde2 = new Kunder
            {
                Epost = "oleolsen@gmail.com",
                Telefon = "33426412"
            };

            var kundeListe = new List<Kunder>();
            kundeListe.Add(kunde1);
            kundeListe.Add(kunde2);

            mockRep.Setup(rep => rep.HentKunder()).ReturnsAsync(kundeListe);

            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.HentKunder() as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal<List<Kunder>>((List<Kunder>)resultat.Value, kundeListe);
        }

        [Fact]
        public async Task TestHentIngenKunder()
        {
            var kundeListe = new List<Kunder>();

            mockRep.Setup(rep => rep.HentKunder()).ReturnsAsync(()=>null);

            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.HentKunder() as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke hente kunder", resultat.Value);
        }
        
        [Fact]
        public async Task TestHentStrekninger()
        {
            var sted1 = new Strekninger
            {
                StedNavn = "Oslo"
            };
            var sted2 = new Strekninger
            {
                StedNavn = "Stavenger"
            };
            var sted3 = new Strekninger
            {
                StedNavn = "Bergen"
            };

            var strekningListe = new List<Strekninger>();
            strekningListe.Add(sted3);
            strekningListe.Add(sted2);
            strekningListe.Add(sted1);

            mockRep.Setup(rep => rep.HentStrekninger()).ReturnsAsync(strekningListe);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.HentStrekninger() as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal<List<Strekninger>>((List<Strekninger>)resultat.Value, strekningListe);
        }

        [Fact]
        public async Task TestHentIngenStrekninger()
        {
            var strekningListe = new List<Strekninger>();

            mockRep.Setup(rep => rep.HentStrekninger()).ReturnsAsync(()=>null);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.HentStrekninger() as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke hente strekninger", resultat.Value);
        }
        
        [Fact]
        public async Task TestLagreType()
        {
            var type1 = new BillettType
            {
                Type = "voksen",
                Pris =  200
            };

            mockRep.Setup(k => k.LagreType(type1)).ReturnsAsync(true);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.LagreType(type1) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal("Billett type lagret", resultat.Value);
        }

        [Fact]
        public async Task TestLagreTypeIkkeOk()
        {
            var type1 = new BillettType
            {
                Type = "voksen",
                Pris = 200
            };

            mockRep.Setup(k => k.LagreType(type1)).ReturnsAsync(false);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.LagreType(type1) as BadRequestObjectResult;

            Assert.Equal((int)HttpStatusCode.BadRequest, resultat.StatusCode);
            Assert.Equal("Verdiene er ikke valide", resultat.Value);
        }
        
        [Fact]
        public async Task TestLagreStrekning()
        {
            var strekning = new Strekninger
            {
                StedNavn = "Bergen"
            };

            mockRep.Setup(k => k.LagreStrekning(strekning)).ReturnsAsync(true);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.LagreStrekning(strekning) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal("Ny Strekning lagret", resultat.Value);
        }

        [Fact]
        public async Task TestLagreStrekningIkkeOk()
        {
            var strekning = new Strekninger
            {
                StedNavn = "Bergen"
            };

            mockRep.Setup(k => k.LagreStrekning(strekning)).ReturnsAsync(false);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.LagreStrekning(strekning) as BadRequestObjectResult;

            Assert.Equal((int)HttpStatusCode.BadRequest, resultat.StatusCode);
            Assert.Equal("Verdiene er ikke valide", resultat.Value);
        }
        
        [Fact]
        public async Task TestLagreTidsplan()
        {
            var tidsplan = new Tidsplaner
            {
                ReiseTid = "07:35",
                Ukedag = 3
            };

            mockRep.Setup(k => k.LagreTidsplan(tidsplan)).ReturnsAsync(true);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.LagreTidsplan(tidsplan) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal("Ny tidsplan lagret", resultat.Value);
        }

        [Fact]
        public async Task TestLagreTidsplanIkkeOk()
        {
            var tidsplan = new Tidsplaner
            {
                ReiseTid = "07:35",
                Ukedag = 3
            };

            mockRep.Setup(k => k.LagreTidsplan(tidsplan)).ReturnsAsync(false);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.LagreTidsplan(tidsplan) as BadRequestObjectResult;

            Assert.Equal((int)HttpStatusCode.BadRequest, resultat.StatusCode);
            Assert.Equal("Verdiene er ikke valide", resultat.Value);
        }
        
        [Fact]
        public async Task TestLagreRute()
        {
            var strekning = new Strekninger
            {
                SId = 1,
                StedNavn = "Bergen"
            };

            var tidsplan = new Tidsplaner
            {
                TId = 1,
                ReiseTid = "07:35",
                Ukedag = 3
            };

            var rute = new Rute
            {
                StedNavn = strekning,
                TId = tidsplan
            };

            mockRep.Setup(k => k.LagreRute(1, 1)).ReturnsAsync(true);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.LagreRute(1, 1) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal("Ny Rute lagret", resultat.Value);
        }

        [Fact]
        public async Task TestLagreRuteIkkeOk()
        {
            var strekning = new Strekninger
            {
                SId = 1,
                StedNavn = "Bergen"
            };

            var tidsplan = new Tidsplaner
            {
                TId = 1,
                ReiseTid = "07:35",
                Ukedag = 3
            };

            var rute = new Rute
            {
                StedNavn = strekning,
                TId = tidsplan
            };

            mockRep.Setup(k => k.LagreRute(1, 1)).ReturnsAsync(false);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.LagreRute(1, 1) as BadRequestObjectResult;

            Assert.Equal((int)HttpStatusCode.BadRequest, resultat.StatusCode);
            Assert.Equal("Verdiene er ikke valide", resultat.Value);
        }
        
        [Fact]
        public async Task TestSlettType()
        {
            mockRep.Setup(k => k.SlettType(1)).ReturnsAsync(true);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.SlettType(1) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal("Billett type slettet", resultat.Value);
        }

        [Fact]
        public async Task TestSlettTypeIkkeOk()
        {
            mockRep.Setup(k => k.SlettType(It.IsAny<int>())).ReturnsAsync(false);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.SlettType(It.IsAny<int>()) as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke slette raden fra databasen", resultat.Value);
        }
        
        [Fact]
        public async Task TestSlettStrekning()
        {
            mockRep.Setup(k => k.SlettStrekning(1)).ReturnsAsync(true);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.SlettStrekning(1) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal("Strekning slettet", resultat.Value);
        }

        [Fact]
        public async Task TestSlettStrekningIkkeOk()
        {
            mockRep.Setup(k => k.SlettStrekning(1)).ReturnsAsync(false);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.SlettStrekning(1) as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke slette raden fra databasen", resultat.Value);
        }
        
        [Fact]
        public async Task TestSlettKunde()
        {
            mockRep.Setup(k => k.SlettKunde(1)).ReturnsAsync(true);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.SlettKunde(1) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal("Kunde slettet", resultat.Value);
        }

        [Fact]
        public async Task TestSlettKundeIkkeOk()
        {
            mockRep.Setup(k => k.SlettKunde(1)).ReturnsAsync(false);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.SlettKunde(1) as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke slette raden fra databasen", resultat.Value);
        }
        
        [Fact]
        public async Task TestSlettBillett()
        {
            mockRep.Setup(k => k.SlettBillett(1)).ReturnsAsync(true);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.SlettBillett(1) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal("Billett slettet", resultat.Value);
        }

        [Fact]
        public async Task TestSlettBillettIkkeOk()
        {
            mockRep.Setup(k => k.SlettBillett(1)).ReturnsAsync(false);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.SlettBillett(1) as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke slette raden fra databasen", resultat.Value);
        }
        
        [Fact]
        public async Task TestSlettBestilling()
        {
            mockRep.Setup(k => k.SlettBestilling(1)).ReturnsAsync(true);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.SlettBestilling(1) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal("Bestilling slettet", resultat.Value);
        }
        
        [Fact]
        public async Task TestSlettBestillingIkkeOk()
        {
            mockRep.Setup(k => k.SlettBestilling(1)).ReturnsAsync(false);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.SlettBestilling(1) as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke slette raden fra databasen", resultat.Value);
        }
        
        [Fact]
        public async Task TestSlettRute()
        {
            mockRep.Setup(k => k.SlettRute(1)).ReturnsAsync(true);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.SlettRute(1) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal("Rute slettet", resultat.Value);
        }


        [Fact]
        public async Task TestSlettRuteIkkeOk()
        {
            mockRep.Setup(k => k.SlettRute(1)).ReturnsAsync(false);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.SlettRute(1) as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke slette raden fra databasen", resultat.Value);
        }
        
        [Fact]
        public async Task TestSlettTidsplan()
        {
            mockRep.Setup(k => k.SlettTidsplan(1)).ReturnsAsync(true);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.SlettTidsplan(1) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal("Tidsplan slettet", resultat.Value);
        }

        [Fact]
        public async Task TestSlettTidsplanIkkeOk()
        {
            mockRep.Setup(k => k.SlettTidsplan(1)).ReturnsAsync(false);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.SlettTidsplan(1) as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke slette raden fra databasen", resultat.Value);
        }
        
        [Fact]
        public async Task TestEndreType()
        {
            mockRep.Setup(k => k.EndreType(It.IsAny<int>(), It.IsAny<BillettType>())).ReturnsAsync(true);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.EndreType(It.IsAny<int>(), It.IsAny<BillettType>()) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal("Billett type endret", resultat.Value);
        }

        [Fact]
        public async Task TestEndreTypeIkkeOk()
        {
            mockRep.Setup(k => k.EndreType(It.IsAny<int>(), It.IsAny<BillettType>())).ReturnsAsync(false);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.EndreType(It.IsAny<int>(), It.IsAny<BillettType>()) as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke oppdatere raden", resultat.Value);
        }
        
        [Fact]
        public async Task TestEndreStrekning()
        {
            mockRep.Setup(k => k.EndreStrekning(It.IsAny<Strekninger>())).ReturnsAsync(true);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.EndreStrekning(It.IsAny<Strekninger>()) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal("Strekning endret", resultat.Value);
        }

        [Fact]
        public async Task TestEndreStrekningIkkeOk()
        {
            mockRep.Setup(k => k.EndreStrekning(It.IsAny<Strekninger>())).ReturnsAsync(false);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.EndreStrekning(It.IsAny<Strekninger>()) as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke oppdatere raden", resultat.Value);
        }
        
        [Fact]
        public async Task TestEndreKunde()
        {
            mockRep.Setup(k => k.EndreKunde(It.IsAny<Kunder>())).ReturnsAsync(true);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.EndreKunde(It.IsAny<Kunder>()) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal("Kunde endret", resultat.Value);
        }

        [Fact]
        public async Task TestEndreKundeIkkeOk()
        {
            mockRep.Setup(k => k.EndreKunde(It.IsAny<Kunder>())).ReturnsAsync(false);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.EndreKunde(It.IsAny<Kunder>()) as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke oppdatere raden", resultat.Value);
        }
        
        [Fact]
        public async Task TestEndreTidsplan()
        {
            mockRep.Setup(k => k.EndreTidsplan(It.IsAny<Tidsplaner>())).ReturnsAsync(true);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.EndreTidsplan(It.IsAny<Tidsplaner>()) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal("Tidsplan endret", resultat.Value);
        }

        [Fact]
        public async Task TestEndreTidsplanIkkeOk()
        {
            mockRep.Setup(k => k.EndreTidsplan(It.IsAny<Tidsplaner>())).ReturnsAsync(false);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.EndreTidsplan(It.IsAny<Tidsplaner>()) as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke oppdatere raden", resultat.Value);
        }
        
        [Fact]
        public async Task TestEndreRute()
        {
            mockRep.Setup(k => k.EndreRute(It.IsAny<int>(), It.IsAny<int>(), It.IsAny<int>())).ReturnsAsync(true);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.EndreRute(It.IsAny<int>(), It.IsAny<int>(), It.IsAny<int>()) as OkObjectResult;

            Assert.Equal((int)HttpStatusCode.OK, resultat.StatusCode);
            Assert.Equal("Rute endret", resultat.Value);
        }

        [Fact]
        public async Task TestEndreRuteIkkeOk()
        {
            mockRep.Setup(k => k.EndreRute(It.IsAny<int>(), It.IsAny<int>(), It.IsAny<int>())).ReturnsAsync(false);
            var bestillingController = new BestillingController(mockRep.Object, mockLog.Object);
            var resultat = await bestillingController.EndreRute(It.IsAny<int>(), It.IsAny<int>(), It.IsAny<int>()) as NotFoundObjectResult;

            Assert.Equal((int)HttpStatusCode.NotFound, resultat.StatusCode);
            Assert.Equal("Kunne ikke oppdatere raden", resultat.Value);
        }
        
    }
}
