using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace oblig3.DAL
{
    public class Kategori
    {
        public int Id { get; set; }
        public string Kategorinavn { get; set; }
    }

    public class Sporsmaal {
        public int Id { get; set; }
        [RegularExpression(@"[a-zA-ZæøåÆØÅ. \-]{2,30}")]
        public string Spors { get; set; }
        public string Svar { get; set; }
        public int Rating { get; set; }

        virtual public Kategori Kategori { get; set; }
    }

    public class FaqContext : DbContext
    {
        public FaqContext(DbContextOptions<FaqContext> options) : base(options)
        {
            Database.EnsureCreated();
        }

        public DbSet<Kategori> Kategorier { get; set; }
        public DbSet<Sporsmaal> Sporsmaalene { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            
            optionsBuilder.UseLazyLoadingProxies();
        }
    }
}
