var json = {};

//function show(elements) {
//    // set cytoscape to div
//    var cy = cytoscape({
//                          container: document.getElementById('cy'),
//                          elements: [
//                            { data: { id: 'a' } },
//                            { data: { id: 'b' } },
//                            { data: { id: 'c' } },
//                            { data: { id: 'd' } },
//                            { data: { id: 'e' } },
//                            { data: { id: 'f' } },
//                            {
//                              data: {
//                                id: 'ab',
//                                source: 'a',
//                                target: 'b'
//                              }
//                            },
//                            {
//                              data: {
//                                id: 'ac',
//                                source: 'a',
//                                target: 'c'
//                              }
//                            }],
//                          layout: {
//                              name: 'random'
//                          }
//                        });
//}

function show(elements_in) {
    // set cytoscape to div
    var cy = cytoscape({
        container: document.getElementById('cy'),
        style: cytoscape.stylesheet()
            .selector('node')
            .css({
                'label': 'data(id)',
                'text-valign': 'center',
                'text-halign': 'center',
                'border-color': 'black',
                'border-opacity': '1',
            })
            .selector('edge')
            .css({
                'curve-style': 'Haystack',
                'haystack-radius': .5,
            }),
        elements: elements_in,
        layout: {
            name: 'circle'
        }
    });

    // if label is edge1, edge2, edge3, then change color to red, blue, green respectively
    cy.edges().forEach(function( ele ){
        if (ele.data('label') == 'edge1') {
            ele.style('line-color', 'red');
        }
        else if (ele.data('label') == 'edge2') {
            ele.style('line-color', 'blue');
        }
        else if (ele.data('label') == 'edge3') {
            ele.style('line-color', 'green');
        }
        });
}

