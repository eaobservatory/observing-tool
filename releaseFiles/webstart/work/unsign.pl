#!/usr/bin/perl

unless (-e unzipped) {
    print "mkdir unzipped\n";
    `mkdir unzipped`;
}

chdir( unzipped ) or die "Cannot cd to unzipped\n";
@jars = `find .. -name "*.jar"`;

foreach $jar (@jars) {
    chomp($jar);
    print "Removing contents\r";
    `rm -rf *`;
    print "Unzipping $jar\r";
    `unzip $jar`;
    print "Removing META-INF\r";
    `rm -rf META-INF`;
    print "jar'ing up $jar\r";
    `jar cf $jar *`;
    print "$jar done\n";
}
