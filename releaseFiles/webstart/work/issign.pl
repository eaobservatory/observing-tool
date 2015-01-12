@jars = `find . -name "*.jar"`;
@signed = ();
@unsigned = ();
$count = 0;

foreach $jar (@jars) {
    print "\r" . $count++ . " of " . $#jars;
    chomp($jar);

    $output = `jarsigner -verify $jar`;

    if ($output =~ "missing") {
        push(@unsigned, $jar);
    }
    else {
        push(@signed, $jar);
    }
}

print "\n" . "-" x 20 . "\n";
print "Unsigned jars\n";
print "-" x 20 . "\n";

foreach $entry (@unsigned) {
	print $entry . "\n";
}

print "-" x 20 . "\n";
print "Signed jars\n";
%signers = ();

foreach $entry (@signed) {
    @verified = `jarsigner -verify -verbose -certs $entry`;

    foreach $line (@verified) {
        chomp($line);
        $line =~ /\s+X.509, CN=(.+),/;
        if (defined $1) {
            $signer = $1;
        }
    }

    my $entries_ref = $signers{$signer};

    if (defined $entries_ref) {
        my @entries = @$entries_ref;
        push(@entries, $entry);
        $signers{$signer} = \@entries;
    }
    else {
        my @entries = ($entry);
        $signers{$signer} = \@entries;
    }
}

@keys = keys %signers;

foreach $key (@keys) {
    print "-" x 20 . "\n";
    print $key . "\n";
    my $array_ref = $signers{$key};
    my @entries = @$array_ref;

    foreach $entry (@entries) {
        print $entry . "\n";
    }
}

print "-" x 20 . "\n";
