using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace oblig3.DAL
{
    public class DBInit
    {
        public static void Seed(IApplicationBuilder app)
        {
            var serviceScope = app.ApplicationServices.CreateScope();

            var db = serviceScope.ServiceProvider.GetService<FaqContext>();

            db.Database.EnsureDeleted();
            db.Database.EnsureCreated();

            var kategori1 = new Kategori
            {
                Kategorinavn = "Billetter"
            };

            var kategori2 = new Kategori
            {
                Kategorinavn = "Bestilling"
            };

            var kategori3 = new Kategori
            {
                Kategorinavn = "Strekninger"
            };

            db.Kategorier.Add(kategori1);
            db.Kategorier.Add(kategori2);
            db.Kategorier.Add(kategori3);


            var s1 = new Sporsmaal
            {
                Kategori = kategori3,
                Spors = "Hvor kan jeg reise?",
                Svar = "Så lenge er det mulig å reise til Oslo, Bergen, Stavanger, Kristiansand og Trondheim.",
                Rating = 13,
            };

            var s2 = new Sporsmaal
            {
                Kategori = kategori2,
                Spors = "Hvor mange billetter kan jeg bestille?",
                Svar = "Du kan kjøpe opptil 9 billetter på samme bestillingen",
                Rating = 5,
            };

            var s3 = new Sporsmaal
            {
                Kategori = kategori1,
                Spors = "Hvordan kan jeg få billett etter bestilling?",
                Svar = "Billett skal sendes til din epost adresse etter godkjent bestilling. Alternativt skal billettene vises på side minebestillinger hvor alle billetene fra samme bestillingen blir utstilt. Slik i framtiden skal det gå å laste ned billettene",
                Rating = 7,
            };

            var s4 = new Sporsmaal
            {
                Kategori = kategori3,
                Spors = "Skal det være mulig å reise til flere steder?",
                Svar = "Planen er å ha reiser til de fleste byer i Norge",
                Rating = 3,
            };

            var s5 = new Sporsmaal
            {
                Kategori = kategori3,
                Spors = "Kan jeg bestille reise til Sverige?",
                Svar = "Nei, reiser kun foregår innenlands",
                Rating = 6,
            };

            var s6 = new Sporsmaal
            {
                Kategori = kategori2,
                Spors = "Kan jeg betale bestillingen med Vipps?",
                Svar = "Ja, Vipps er støttet som betalings alternativ",
                Rating = 11,
            };

            var s7 = new Sporsmaal
            {
                Kategori = kategori1,
                Spors = "Hvordan finner jeg sted som jeg vil reise til?",
                Svar = "På hjemmeside kan man søke etter strekning ved å trykke på inputfeltet 'reise fra / reise til'. Det skal vises mulige strekninger som kan velges ved å trykke på dem. I tillegg kan man finne strekning ved å skrive det inn på inputfeltet. Da vises kun de strekningene som samsvarer med input fra brukeren.",
                Rating = 16,
            };

            var s8 = new Sporsmaal
            {
                Kategori = kategori1,
                Spors = "Hvordan velger jeg dato for reise?",
                Svar = "Datoen kan velges ved å trykke på inputfeltet 'Dato'. Da vises kalendar hvor man kan velge ønskende dato for reise. Man kan velge datoer opptil 3 måneder i framtiden.",
                Rating = 10,
            };

            var s9 = new Sporsmaal
            {
                Kategori = kategori1,
                Spors = "Hvordan velger jeg antallet av billetter?",
                Svar = "Antallet kan velges på seksjon 'Reisende'. Du kan bruke + og - knappene for å justere antallet. Alternativt kan man skrive inn antallet av reisende ved å trykke på tallet. Maks tillatt antallet er 9 billetter ved samme bestillingen.",
                Rating = 8,
            };

            db.Sporsmaalene.Add(s1);
            db.Sporsmaalene.Add(s2);
            db.Sporsmaalene.Add(s3);
            db.Sporsmaalene.Add(s4);
            db.Sporsmaalene.Add(s5);
            db.Sporsmaalene.Add(s6);
            db.Sporsmaalene.Add(s7);
            db.Sporsmaalene.Add(s8);
            db.Sporsmaalene.Add(s9);

            db.SaveChanges();
        }
    }
}
